package org.okay4cloud.okay.shortlink.email;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author wnhyang
 * @date 2023/3/25
 **/
@Component
@RequiredArgsConstructor
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSenderImpl javaMailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendHtmlMail(String to, String subject, Map<String, Object> model) {
        MimeMessage mimeMailMessage;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            Context context = new Context();
            context.setVariables(model);
            String html = templateEngine.process("expire_mail", context);
            mimeMessageHelper.setText(html, true);
            javaMailSender.send(mimeMailMessage);
            LOGGER.info("sendHtmlMail邮件发送成功 {};{};{}", to, subject, model);
        } catch (Exception e) {
            LOGGER.error("sendHtmlMail邮件发送成功 {};{};{}", to, subject, model);
            e.printStackTrace();
        }
    }
}
