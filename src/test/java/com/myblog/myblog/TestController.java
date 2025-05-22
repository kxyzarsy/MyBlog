package com.myblog.myblog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class TestController {
    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void testSendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kxyatxy116@163.com");
        message.setTo("18955192235@163.com");
        message.setSubject("测试邮件");
        message.setText("这是一封测试邮件");
        mailSender.send(message);
        System.out.println("邮件发送成功");
    }
}