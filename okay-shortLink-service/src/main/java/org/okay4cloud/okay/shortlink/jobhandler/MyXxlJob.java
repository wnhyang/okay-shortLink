package org.okay4cloud.okay.shortlink.jobhandler;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.okay4cloud.okay.shortlink.api.entity.LinkMap;
import org.okay4cloud.okay.shortlink.email.Email;
import org.okay4cloud.okay.shortlink.email.EmailService;
import org.okay4cloud.okay.shortlink.email.EmailTemplate;
import org.okay4cloud.okay.shortlink.mapper.LinkMapMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

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
        XxlJobHelper.log(linkMaps.toString());
        if (!CollUtil.isEmpty(linkMaps)) {
            for (LinkMap linkMap : linkMaps) {
                Email email = new Email();
                email.setEmailAddress("wnhyang@139.com");
                email.setSubject("短链接即将过期提醒");
                String content = MessageFormat.format(EmailTemplate.EXPIRE_TEMPLATE, linkMap.getLink(), linkMap.getRemark(), linkMap.getExpireTime());
                email.setContent(content);
                emailService.sendHtmlMail(email);
            }
        }
    }
}
