package org.okay4cloud.okay.shortlink.controller;

import lombok.RequiredArgsConstructor;
import org.okay4cloud.okay.shortlink.service.LinkMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wnhyang
 * @date 2023/1/12
 **/
@RestController
@RequiredArgsConstructor
public class RedirectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectController.class);

    private static final String REDIRECT = "redirect:";

    final LinkMapService linkMapService;

    @Value("${config.info}")
    private String info;

    @GetMapping("/config/info")
    public String info() {
        LOGGER.info(info);
        return info;
    }

    /**
     * 短链接转长链接
     *
     * @param s 短链字符串
     * @return 重定向长链接
     */
    @GetMapping("/{s}")
    public String redirect(@PathVariable("s") String s) {
        LOGGER.info("重定向 {}", s);
        return REDIRECT + linkMapService.redirect(s);
    }
}
