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

/**
 * メール送信に関連するサービスクラス。
 * メールの作成と送信を行います。
 */
@Service
public class SendMailService {
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${SPRING_MAIL_USERNAME}")
    private String springMailUsername;

    /**
     * メールを送信します。
     *
     * @param context メールのコンテキスト情報
     * @param recipientEmail 受信者のメールアドレス
     * @param templateName 使用するテンプレート名
     */
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

            /**
             * メールの本文を取得します。
             *
             * @param templateName 使用するテンプレート名
             * @param context メールのコンテキスト情報
             * @return メールの本文
             */
            private String getMailBody(String templateName, Context context) {
                SpringTemplateEngine templateEngine = new SpringTemplateEngine();
                templateEngine.setTemplateResolver(templateResolver());
                return templateEngine.process(templateName, context);
            }
            
            /**
             * テンプレートリゾルバを設定します。
             *
             * @return テンプレートリゾルバ
             */
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
