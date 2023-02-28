package org.okay4cloud.okay.shortlink.api.feign;

import org.okay4cloud.okay.shortlink.api.constant.ServiceNameConstants;
import org.okay4cloud.okay.shortlink.api.factory.LinkMapFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wnhyang
 * @date 2023/2/22
 **/
@FeignClient(contextId = "linkMapFeignService", value = ServiceNameConstants.SHORTLINK_SERVICE, fallbackFactory = LinkMapFeignFallbackFactory.class)
public interface LinkMapFeignService {


}
