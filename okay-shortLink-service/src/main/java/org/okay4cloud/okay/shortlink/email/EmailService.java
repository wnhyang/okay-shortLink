package org.okay4cloud.okay.shortlink.email;


import cn.hutool.core.util.StrUtil;
import org.okay4cloud.okay.common.core.exception.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/**
 * @author wnhyang
 * @date 2023/3/25
 **/
@Component
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendHtmlMail(Email email) {
        MimeMessage mimeMailMessage;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(email.getEmailAddress());
            mimeMessageHelper.setSubject(email.getSubject());
            if (StrUtil.isNotBlank(email.getContent())) {
                mimeMessageHelper.setText(email.getContent());
            } else {
                throw new ParamException("邮件内容为空");
            }
            javaMailSender.send(mimeMailMessage);
            LOGGER.info("sendHtmlMail邮件发送成功{}", email);
        } catch (Exception e) {
            LOGGER.error("sendHtmlMail邮件发送失败{}", email);
            e.printStackTrace();
        }
    }
}
