package cn.wnhyang.okay.shortlink.controller;

import cn.wnhyang.okay.shortlink.service.LinkMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 重定向
 *
 * @author wnhyang
 * @date 2023/1/12
 **/
@Controller
@RequiredArgsConstructor
public class RedirectController {

    private static final String REDIRECT = "redirect:";

    private final LinkMapService linkMapService;

    /**
     * nacos配置读取
     */
    @Value("${config.info}")
    private String info;

    /**
     * nacos配置
     *
     * @return String
     */
    @ResponseBody
    @GetMapping("/config/info")
    public String info() {
        return info;
    }

    /**
     * 短链接转长链接
     *
     * @param code 短链字符串
     * @return 重定向长链接
     */
    @GetMapping("/r/{code}")
    public String redirect(@PathVariable("code") String code) {
        return REDIRECT + linkMapService.redirect(code);
    }


}
