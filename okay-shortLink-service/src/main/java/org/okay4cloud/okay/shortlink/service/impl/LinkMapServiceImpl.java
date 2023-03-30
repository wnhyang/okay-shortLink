package org.okay4cloud.okay.shortlink.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.okay4cloud.okay.common.core.exception.CheckedException;
import org.okay4cloud.okay.common.core.util.RedisUtils;
import org.okay4cloud.okay.shortlink.api.dto.LinkMapDTO;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;
import org.okay4cloud.okay.shortlink.api.vo.Visits;
import org.okay4cloud.okay.shortlink.api.vo.VisitsVO;
import org.okay4cloud.okay.shortlink.constant.CacheConstants;
import org.okay4cloud.okay.shortlink.mapper.LinkMapMapper;
import org.okay4cloud.okay.shortlink.service.LinkMapService;
import org.okay4cloud.okay.shortlink.util.Encoder62;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wnhyang
 * @since 2023-02-21
 */
@Service
@RequiredArgsConstructor
public class LinkMapServiceImpl extends ServiceImpl<LinkMapMapper, LinkMap> implements LinkMapService {

    private final ValueOperations<String, String> valueOperations;

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkMapServiceImpl.class);

    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_32_fixed();

    private static final String STR = "=R@T#D";

    /**
     * 根据短链接重定向到长链接
     * 短链接有效条件
     * 1、数据库存在且未删除
     * 2、过期时间内
     *
     * @param code 短链接
     * @return 长链接
     */
    @Override
    @Cacheable(value = CacheConstants.LINK, key = "#code", unless = "#result==null")
    public String redirect(String code) {
        LinkMap linkMap = baseMapper.selectOne(new LambdaQueryWrapper<LinkMap>()
                .eq(LinkMap::getCode, code)
                .ge(LinkMap::getExpireTime, LocalDateTime.now()));
        if (null == linkMap) {
            throw new CheckedException("链接不存在或已失效");
        }
        String now = LocalDate.now().toString();
        String key = RedisUtils.getKey(CacheConstants.LINK_VISITS, linkMap.getId(), now);
        Object value = valueOperations.get(key);
        if (null != value) {
            valueOperations.increment(key);
        } else {
            valueOperations.set(key, "1", RedisUtils.TIME_OUT_30, TimeUnit.DAYS);
        }
        return linkMap.getLink();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveLinkMap(LinkMapDTO linkMapDTO) {
        LOGGER.debug("链接映射:{}", linkMapDTO);
        LinkMap linkMap = new LinkMap();
        BeanUtil.copyProperties(linkMapDTO, linkMap);
        // 1、查询是否已存在改长链接
        // 可能存在以下情况
        //  a、不存在，算法生成短链接，要求，短链唯一，过期时间默认90天
        //  b、存在  b.1、逻辑删除，逻辑找回并更新过期时间等信息（考虑之下已经改为物理删除）
        //          b.2、未删除过期，更新延期

        String link = linkMap.getLink();
        LinkMap one = baseMapper.selectOne(new LambdaQueryWrapper<LinkMap>()
                .eq(LinkMap::getLink, link));
        if (null == one) {
            while (true) {
                LOGGER.debug("链接为:{}", link);
                // 1、MurmurHash加密
                HashCode hashCode = HASH_FUNCTION.hashString(link, StandardCharsets.UTF_8);

                String code = "";
                // 2、短链
                try {
                    code = Encoder62.encode62(hashCode.padToLong());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LOGGER.debug("短链字符串:{}", code);
                linkMap.setCode(code);

                //3、直接插数据库，看是否已存在短链
                if (null == linkMap.getExpireTime()) {
                    linkMap.setExpireTime(LocalDateTime.now().plusDays(90));
                }
                int insert = baseMapper.insert(linkMap);
                if (insert < 1) {
                    link += STR;
                    LOGGER.debug("短链接已存在,新长链接为:{}", link);
                } else {
                    LOGGER.debug("长链接:{},生成短链:{}", link, code);
                    return Boolean.TRUE;
                }
            }
        } else {
            one.setExpireTime(LocalDateTime.now());
            baseMapper.updateById(one);
            return Boolean.TRUE;
        }
    }

    @Override
    @Cacheable(value = CacheConstants.LINK_DETAILS, key = "#id", unless = "#result == null")
    public LinkMap getLinkMapById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CacheConstants.LINK_DETAILS}, allEntries = true)
    public Boolean updateLinkMapById(LinkMap linkMap) {
        LinkMap entity = new LinkMap();
        entity.setId(linkMap.getId());
        entity.setType(linkMap.getType());
        entity.setRemark(linkMap.getRemark());
        entity.setExpireTime(linkMap.getExpireTime());
        entity.setEmail(linkMap.getEmail());
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CacheConstants.LINK_DETAILS, CacheConstants.LINK}, allEntries = true)
    public Boolean deleteLinkMapByIds(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public Visits getVisits(Long id) {
        return getVisits(id, RedisUtils.TIME_OUT_30);
    }

    @Override
    public Visits getVisits(Long id, long days) {
        LinkMap linkMap = baseMapper.selectById(id);
        if (null == linkMap) {
            return null;
        }
        LocalDate now = LocalDate.now();
        Visits visits = new Visits();
        int total = 0;
        List<VisitsVO> visitsVOList = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            String date = now.minusDays(i).toString();
            String key = RedisUtils.getKey(CacheConstants.LINK_VISITS, id, date);
            String count = valueOperations.get(key);
            if (null == count || "0".equals(count)) {
                continue;
            }
            total += Integer.parseInt(count);
            VisitsVO visitsVO = new VisitsVO();
            visitsVO.setDate(date);
            visitsVO.setCount(count);
            visitsVOList.add(visitsVO);
        }
        visits.setTotal(total);
        visits.setVisitsVOList(visitsVOList);
        return visits;
    }

    @Override
    @CacheEvict(value = {CacheConstants.LINK_DETAILS, CacheConstants.LINK}, allEntries = true)
    public void clearLinkMapCache() {

    }

}
