package com.myblog.myblog.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("kxyatxy116@163.com");
        helper.setTo(toEmail);
        helper.setSubject("【MyBlog】注册验证码");

        String htmlContent = buildEmailContent(code);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String buildEmailContent(String code) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                + "<h2 style='color: #2d8cf0; text-align: center; margin-bottom: 30px;'>欢迎加入MyBlog</h2>"
                + "<p style='font-size: 16px; color: #333;'>感谢您注册MyBlog！以下是您的验证码：</p>"
                + "<div style='background: #f8f8f8; padding: 15px; margin: 25px 0; text-align: center; border-radius: 6px;'>"
                + "<h1 style='color: #2d8cf0; margin: 0; font-size: 32px;'>" + code + "</h1>"
                + "</div>"
                + "<p style='font-size: 14px; color: #666;'>"
                + "此验证码 <strong>5分钟</strong> 内有效，请尽快完成注册。<br/>"
                + "如果您未进行此操作，请忽略本邮件。"
                + "</p>"
                + "<hr style='border: none; border-top: 1px solid #e0e0e0; margin: 25px 0;'>"
                + "<p style='font-size: 12px; color: #999; text-align: center;'>"
                + "此为系统邮件，请勿直接回复。<br/>"
                + "如有疑问，请联系 <a href='mailto:support@myblog.com' style='color: #2d8cf0;'>support@myblog.com</a>"
                + "</p>"
                + "</div>";
    }
}