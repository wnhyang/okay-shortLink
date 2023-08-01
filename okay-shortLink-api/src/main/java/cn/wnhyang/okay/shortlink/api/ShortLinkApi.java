package cn.wnhyang.okay.shortlink.api;

import cn.wnhyang.okay.framework.common.enums.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wnhyang
 * @date 2023/7/30
 **/
@FeignClient(name = ApiConstants.OKAY_SHORTLINK_NAME)
public interface ShortLinkApi {
}
