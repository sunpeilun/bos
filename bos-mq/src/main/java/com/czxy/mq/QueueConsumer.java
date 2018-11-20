package com.czxy.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;


/**
 * 消费者
 */
@Component
public class QueueConsumer {
    // 监听器
    // destination队列的名字
    // 方法名字随意
    @JmsListener(destination = "java1.bos.queue")
    public void reciveMessage(Message message) throws Exception{
        MapMessage mapMessage = (MapMessage)message;
        String info = mapMessage.getString("info1");

        System.out.println(info);
    }


}
