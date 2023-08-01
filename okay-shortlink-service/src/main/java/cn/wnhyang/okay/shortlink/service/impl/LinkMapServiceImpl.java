package cn.wnhyang.okay.shortlink.service.impl;

import cn.wnhyang.okay.framework.common.exception.util.ServiceExceptionUtil;
import cn.wnhyang.okay.framework.common.pojo.PageResult;
import cn.wnhyang.okay.shortlink.constant.CacheConstants;
import cn.wnhyang.okay.shortlink.convert.linkmap.LinkMapConvert;
import cn.wnhyang.okay.shortlink.entity.LinkMapDO;
import cn.wnhyang.okay.shortlink.mapper.LinkMapMapper;
import cn.wnhyang.okay.shortlink.service.LinkMapService;
import cn.wnhyang.okay.shortlink.util.Encoder62;
import cn.wnhyang.okay.shortlink.vo.Visits;
import cn.wnhyang.okay.shortlink.vo.VisitsVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapCreateReqVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapPageReqVO;
import cn.wnhyang.okay.shortlink.vo.linkmap.LinkMapUpdateReqVO;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 服务实现类
 *
 * @author wnhyang
 * @since 2023-02-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LinkMapServiceImpl implements LinkMapService {

    private final LinkMapMapper linkMapMapper;

    private final ValueOperations<String, String> valueOperations;

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
    @Cacheable(value = CacheConstants.LINK_CODE, key = "#code", unless = "#result==null")
    public String redirect(String code) {
        LinkMapDO linkMap = linkMapMapper.selectOne(
                LinkMapDO::getCode, code, LinkMapDO::getExpireTime, LocalDateTime.now());

        if (null == linkMap) {
            throw ServiceExceptionUtil.exception(345, "链接不存在或已失效");
        }
        String now = LocalDate.now().toString();
        String key = CacheConstants.LINK_VISITS + linkMap.getId() + now;
        Object value = valueOperations.get(key);
        if (null != value) {
            valueOperations.increment(key);
        } else {
            valueOperations.set(key, "1", CacheConstants.TIME_OUT_30, TimeUnit.DAYS);
        }
        return linkMap.getLink();
    }

    @Override
    public Visits getVisits(Long id) {
        return getVisits(id, CacheConstants.TIME_OUT_30);
    }

    @Override
    public Visits getVisits(Long id, long days) {
        LinkMapDO linkMap = linkMapMapper.selectById(id);
        if (null == linkMap) {
            return null;
        }
        LocalDate now = LocalDate.now();
        Visits visits = new Visits();
        int total = 0;
        List<VisitsVO> visitsVOList = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            String date = now.minusDays(i).toString();
            String key = CacheConstants.LINK_VISITS + id + date;
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
    @CacheEvict(value = {CacheConstants.LINK_DETAILS, CacheConstants.LINK_CODE}, allEntries = true)
    public void clearLinkMapCache() {

    }

    @Override
    public PageResult<LinkMapDO> getLinkMapPage(LinkMapPageReqVO reqVO) {
        return linkMapMapper.selectPage(reqVO);
    }

    @Override
    public Long createLinkMap(LinkMapCreateReqVO reqVO) {
        log.debug("链接映射:{}", reqVO);
        LinkMapDO linkMap = LinkMapConvert.INSTANCE.convert(reqVO);
        // 1、查询是否已存在改长链接
        // 可能存在以下情况
        //  a、不存在，算法生成短链接，要求，短链唯一，过期时间默认90天
        //  b、存在  b.1、逻辑删除，逻辑找回并更新过期时间等信息（考虑之下已经改为物理删除）
        //          b.2、未删除过期，更新延期

        String link = linkMap.getLink();
        LinkMapDO one = linkMapMapper.selectOne(LinkMapDO::getLink, link);
        if (null == one) {
            while (true) {
                log.debug("链接为:{}", link);
                // 1、MurmurHash加密
                HashCode hashCode = HASH_FUNCTION.hashString(link, StandardCharsets.UTF_8);

                String code = "";
                // 2、短链
                try {
                    code = Encoder62.encode62(hashCode.padToLong());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.debug("短链字符串:{}", code);
                linkMap.setCode(code);

                //3、直接插数据库，看是否已存在短链
                if (null == linkMap.getExpireTime()) {
                    linkMap.setExpireTime(LocalDateTime.now().plusDays(90));
                }
                int insert = linkMapMapper.insert(linkMap);
                if (insert < 1) {
                    link += STR;
                    log.debug("短链接已存在,新长链接为:{}", link);
                } else {
                    log.debug("长链接:{},生成短链:{}", link, code);
                    return linkMap.getId();
                }
            }
        } else {
            one.setExpireTime(LocalDateTime.now());
            linkMapMapper.updateById(one);
            return one.getId();
        }
    }

    @Override
    @CacheEvict(value = {CacheConstants.LINK_DETAILS}, allEntries = true)
    public void updateLinkMap(LinkMapUpdateReqVO reqVO) {
        LinkMapDO linkMap = LinkMapConvert.INSTANCE.convert(reqVO);
        linkMapMapper.updateById(linkMap);
    }

    @Override
    @Cacheable(value = CacheConstants.LINK_DETAILS, key = "#id", unless = "#result == null")
    public LinkMapDO getLinkMap(Long id) {
        return linkMapMapper.selectById(id);
    }

    @Override
    public List<LinkMapDO> getLinkMapList() {
        return linkMapMapper.selectList();
    }

    @Override
    @CacheEvict(value = {CacheConstants.LINK_DETAILS, CacheConstants.LINK_CODE}, allEntries = true)
    public void deleteLinkMap(Long id) {
        linkMapMapper.deleteById(id);
    }

}
