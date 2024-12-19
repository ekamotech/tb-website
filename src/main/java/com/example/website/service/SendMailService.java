package com.example.website.service;

import java.nio.charset.StandardCharsets;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
public class SendMailService {
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${SPRING_MAIL_USERNAME}")
    private String springMailUsername;

    public void sendMail(Context context, String recipientEmail, String templateName) {

        javaMailSender.send(new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
                helper.setFrom(springMailUsername);
                helper.setTo(recipientEmail);
                helper.setSubject((String) context.getVariable("title"));
                helper.setText(getMailBody(templateName, context), true);
            }

            private String getMailBody(String templateName, Context context) {
                SpringTemplateEngine templateEngine = new SpringTemplateEngine();
                templateEngine.setTemplateResolver(templateResolver());
                return templateEngine.process(templateName, context);
            }
            
            private ClassLoaderTemplateResolver templateResolver() {
                ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
                resolver.setPrefix("mailtemplates/");
                resolver.setSuffix(".html");
                resolver.setTemplateMode("HTML");
                resolver.setCharacterEncoding("UTF-8");
                resolver.setCacheable(true);
                return resolver;
            }
            
        });

    }

}
