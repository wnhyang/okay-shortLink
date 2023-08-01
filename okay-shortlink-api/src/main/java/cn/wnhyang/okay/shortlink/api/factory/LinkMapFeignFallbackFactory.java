package cn.wnhyang.okay.shortlink.api.factory;

import cn.wnhyang.okay.shortlink.api.ShortLinkApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author wnhyang
 * @date 2023/2/22
 **/
@Component
@Slf4j
public class LinkMapFeignFallbackFactory implements FallbackFactory<ShortLinkApi> {

    @Override
    public ShortLinkApi create(Throwable cause) {
        log.error("短链接服务调用失败:{}", cause.getMessage());
        return new ShortLinkApi() {
        };
    }
}
