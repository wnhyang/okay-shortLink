package cn.wnhyang.okay.shortlink.email;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class EmailService {

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
            log.info("sendHtmlMail邮件发送成功 {};{};{}", to, subject, model);
        } catch (Exception e) {
            log.error("sendHtmlMail邮件发送成功 {};{};{}", to, subject, model);
            e.printStackTrace();
        }
    }
}
