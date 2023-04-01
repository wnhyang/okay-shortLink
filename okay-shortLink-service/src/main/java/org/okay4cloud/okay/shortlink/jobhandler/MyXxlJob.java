package org.okay4cloud.okay.shortlink.jobhandler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;
import org.okay4cloud.okay.shortlink.email.EmailService;
import org.okay4cloud.okay.shortlink.mapper.LinkMapMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wnhyang
 * @date 2023/3/24
 **/
@Component
@RequiredArgsConstructor
public class MyXxlJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyXxlJob.class);

    private final LinkMapMapper linkMapMapper;

    private final EmailService emailService;

    /**
     * 扫表，3天内过期的进行邮件通知
     */
    @XxlJob("emailJobHandler")
    public void emailJobHandler() {
        LOGGER.info("emailJobHandler exec......");
        XxlJobHelper.log("XXL-JOB, emailJobHandler.");
        LocalDate now = LocalDate.now();
        LocalDate now3 = now.plusDays(3);
        // 用户分组发邮件
        List<LinkMap> linkMaps = linkMapMapper.selectList(new LambdaQueryWrapper<LinkMap>()
                .ge(LinkMap::getExpireTime, now)
                .le(LinkMap::getExpireTime, now3));
        LOGGER.info("即将过期的短链接数量{}", linkMaps.size());
        XxlJobHelper.log("即将过期的短链接数量{}", linkMaps.size());
        if (!CollUtil.isEmpty(linkMaps)) {
            Map<String, List<LinkMap>> collect = linkMaps.stream().collect(Collectors.groupingBy(LinkMap::getEmail));
            LOGGER.info("分组后{}", collect);
            for (Map.Entry<String, List<LinkMap>> entry : collect.entrySet()) {
                String to = entry.getKey();
                if (StrUtil.isNotBlank(to)) {
                    Map<String, Object> model = new HashMap<>(8);
                    model.put("subject", "短链接即将过期提醒");
                    model.put("title", "您的短链接将在三天内失效，请务必确认是否还需要，过期的前三天我们都会发送此邮件来向您确认，即将过期的短链接如下：");
                    List<LinkMap> list = entry.getValue();
                    model.put("list", list);
                    model.put("instance", UUID.randomUUID().toString());
                    emailService.sendHtmlMail(to, "短链接即将过期提醒", model);
                    LOGGER.info("发给 {} 数量{}", to, list.size());
                    XxlJobHelper.log("发给 {} 数量{}", to, list.size());
                }
            }
        }
    }
}
