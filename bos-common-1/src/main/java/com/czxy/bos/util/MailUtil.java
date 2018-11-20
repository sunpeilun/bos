package com.czxy.bos.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {

    public static void sendMail(String to,String subject,String text) throws  Exception{
        //1 创建属性对象
        Properties props = new Properties();
        //2 设置邮件服务器的地址  smtp.163.com       smtp.126.com    smtp.qq.com   smtp.gmail.com
        props.put("mail.smtp.host","smtp.163.com");
        //3 设置是否打开校验 , 是否验证用户名和密码   必须要设置为 true
        props.put("mail.smtp.auth",true);
        //4 获取与邮件服务器的连接
        Session session = Session.getDefaultInstance(props);
        //5 新建一封邮件
        MimeMessage message = new MimeMessage(session);
        //6 设置发送者
        InternetAddress fromAddress = new InternetAddress("sqczdx@163.com");
        message.setFrom(fromAddress);
        //7 设置接收者
        InternetAddress toAddress = new InternetAddress(to);
        //  to 接收者   cc  抄送    bcc 密送
        message.setRecipient(Message.RecipientType.TO,toAddress);
        //8 设置主题
        message.setSubject(subject);
        //9 设置正文
//        message.setText(text);
        //第一个参数：内容
        //第二个参数：内容格式
        message.setContent(text,"text/html;charset=utf-8");
        //10 坐火箭
        Transport transport = session.getTransport("smtp");//参数必须添加
        transport.connect("sqczdx@163.com","q7w8e9a4s5d6");// 密码是授权码
        // 所有的接收者  to  cc  bcc
        transport.sendMessage(message,message.getAllRecipients());
        // 关闭连接
        transport.close();

        System.out.println("ok");
    }

    public static void main(String[] args) {
        try {
            MailUtil.sendMail("1638064027@qq.com","好好学习","天天向上");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
