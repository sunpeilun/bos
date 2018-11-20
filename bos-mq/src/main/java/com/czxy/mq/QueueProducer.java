package com.czxy.mq;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Queue;

/**
 * 生产者
 */
@Component
@EnableScheduling// 开启定时任务
public class QueueProducer {

    // 队列
    @Autowired
    private Queue queue;


    // 发送消息JmsMessagingTemplate
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    int count = 0;

    @Scheduled(fixedDelay = 6000)// 每6秒执行一次这个方法
    public void send() throws Exception{
        // 创建一个消息
        MapMessage mapMessage = new ActiveMQMapMessage();
        mapMessage.setString("info1","快要下课了，肚子真饿了"+(count++));
        // 发送
        //convertAndSend 确认并发送
        // 第一个参数：队列对象
        // 第二个参数：消息内容
        jmsMessagingTemplate.convertAndSend(queue,mapMessage);

    }



}
