package org.okay4cloud.okay.shortlink.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.okay4cloud.okay.common.core.exception.CheckedException;
import org.okay4cloud.okay.common.core.util.R;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;
import org.okay4cloud.okay.shortlink.mapper.LinkMapMapper;
import org.okay4cloud.okay.shortlink.service.LinkMapService;
import org.okay4cloud.okay.shortlink.util.Encoder62;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wnhyang
 * @since 2023-02-21
 */
@Service
public class LinkMapServiceImpl extends ServiceImpl<LinkMapMapper, LinkMap> implements LinkMapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkMapServiceImpl.class);

    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_32_fixed();

//    private static final BloomFilter<String> BLOOM_FILTER = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), Encoder62.SIZE);

    private static final String STR = "=R@T#D";

    private final LinkMapMapper linkMapMapper;

    public LinkMapServiceImpl(LinkMapMapper linkMapMapper) {
        this.linkMapMapper = linkMapMapper;
    }

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
    public String redirect(String code) {
        LinkMap linkMap = linkMapMapper.selectOne(new LambdaQueryWrapper<LinkMap>().eq(LinkMap::getCode, code).ge(LinkMap::getExpireTime, LocalDateTime.now()));
        if (linkMap == null) {
            throw new CheckedException("短链接不存在或已失效");
        }
        return linkMap.getLink();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> linkMapWithBloomFilter(String link) {
        String code = "";
//        while (true) {
//            LOGGER.debug("link:{}", link);
//            // 1、MurmurHash加密
//            HashCode hashCode = HASH_FUNCTION.hashString(link, StandardCharsets.UTF_8);
//
//            // 2、短链
//            try {
//                code = Encoder62.encode62(hashCode.padToLong());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            LOGGER.debug("code:{}", code);
//
//            //3、布隆过滤器
//            boolean contain = BLOOM_FILTER.mightContain(code);
//
//            LinkMap linkMap = new LinkMap();
//            linkMap.setCode(code);
//            linkMap.setLink(link);
//            if (contain) {
//                link += STR;
//                LOGGER.debug("contain,new code:{}", code);
//            } else {
//                linkMapMapper.insert(linkMap);
//                BLOOM_FILTER.put(code);
//                LOGGER.debug("not contain,put({})", code);
//                break;
//            }
//        }
        return R.ok(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveLinkMap(LinkMap linkMap) {

        LOGGER.debug("链接映射:{}", linkMap);
        // 1、查询是否已存在改长链接
        // 可能存在以下情况
        //  a、不存在，算法生成短链接，要求，短链唯一，过期时间默认90天
        //  b、存在  b.1、逻辑删除，逻辑找回并更新过期时间等信息（考虑之下已经改为物理删除）
        //          b.2、未删除过期，更新延期

        String link = linkMap.getLink();
        LinkMap one = linkMapMapper.selectOne(new LambdaQueryWrapper<LinkMap>().eq(LinkMap::getLink, link));
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
                int insert = linkMapMapper.insert(linkMap);
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
            linkMapMapper.updateById(one);
            return Boolean.TRUE;
        }
    }
}
