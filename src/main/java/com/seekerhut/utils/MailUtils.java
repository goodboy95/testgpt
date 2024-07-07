package com.seekerhut.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils {
    public static boolean SendMail(String receiver, String title, String content) {
        // 配置邮件服务器属性
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.qq.com"); // SMTP服务器地址
        properties.put("mail.smtp.port", "587"); // SMTP端口号，通常为25或587
        properties.put("mail.smtp.auth", "true"); // 是否需要认证
        properties.put("mail.smtp.starttls.enable", "true"); // 是否启用TLS
        // 获取默认的Session对象
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("501217662@qq.com", "xrfvbrkksdwnbifg");
            }
        });
        try {
            // 创建一个默认的MimeMessage对象
            MimeMessage message = new MimeMessage(session);
            // 设置发件人
            message.setFrom(new InternetAddress("501217662@qq.com"));
            // 设置收件人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            // 设置邮件主题
            message.setSubject(title);
            // 设置邮件正文
            message.setText(content);
            // 发送邮件
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }
}
