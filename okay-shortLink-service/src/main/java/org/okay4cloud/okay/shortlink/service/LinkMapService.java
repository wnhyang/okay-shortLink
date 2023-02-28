package org.okay4cloud.okay.shortlink.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.okay4cloud.okay.common.core.util.R;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wnhyang
 * @since 2023-02-21
 */
public interface LinkMapService extends IService<LinkMap> {
    /**
     * 重定向
     *
     * @param code 短链接
     * @return 长链接
     */
    String redirect(String code);

    /**
     * 长链接转为短链接，使用布隆过滤器
     *
     * @param link 长链接
     * @return 短链接
     */
    R<String> linkMapWithBloomFilter(String link);

    /**
     * 添加链接映射
     *
     * @param linkMap
     * @return
     */
    Boolean saveLinkMap(LinkMap linkMap);
}
