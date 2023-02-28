package org.okay4cloud.okay.shortlink.api.factory;

import org.okay4cloud.okay.shortlink.api.feign.LinkMapFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author wnhyang
 * @date 2023/2/22
 **/
@Component
public class LinkMapFeignFallbackFactory implements FallbackFactory<LinkMapFeignService> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkMapFeignFallbackFactory.class);


    @Override
    public LinkMapFeignService create(Throwable cause) {
        LOGGER.error("短链接服务调用失败:{}", cause.getMessage());
        return new LinkMapFeignService() {
        };
    }
}
