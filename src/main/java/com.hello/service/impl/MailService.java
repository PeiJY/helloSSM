package com.hello.service.impl;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;

public class MailService {
    private static final String HOST = "smtp.qq.com";
    private static final Integer PORT = 465;
    private static final String USERNAME = "295169719@qq.com";
    private static final String PASSWORD = "zmavnapvhmxwbhch";
    private static final String EMAILFORM = "295169719@qq.com";
    private static JavaMailSenderImpl mailSender = createMailSender();
    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private static JavaMailSenderImpl createMailSender() {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "50000");
        p.setProperty("mail.smtp.connectiontimeout", "50000");
        p.setProperty("mail.smtp.writetimeout", "50000");
        p.setProperty("mail.smtp.auth", "true");
        p.put("mail.debug", "true");
        //**********************
        p.setProperty("mail.smtp.socketFactory.port", Integer.toString(PORT));//设置ssl端口
        p.setProperty("mail.smtp.socketFactory.fallback", "false");
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.setProperty("mail.smtp.ssl.enable", "true");
         //************************
        /*
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }Session
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.ssl.socketFactory", sf);
        */
        sender.setJavaMailProperties(p);
        return sender;
    }

    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param subject 主题
     * @param html 发送内容
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static String sendHtmlMail(String to, String subject, String html) throws MessagingException, UnsupportedEncodingException {
        String result = "";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFORM, "SUSTechFM");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        result+="email setting finished.";
        try {
            mailSender.send(mimeMessage);
        }
        catch (Exception e){
            result+=e.getMessage()+".";
        }

        result+="email sending finished.";
        return result;
    }
}
