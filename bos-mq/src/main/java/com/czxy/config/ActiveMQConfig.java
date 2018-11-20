package com.czxy.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;
import javax.jms.Topic;

@Configuration
public class ActiveMQConfig {

    // 提供生产bean的方法
    @Bean
    public Queue createQueue(){
        return new ActiveMQQueue("java1.bos.queue");
    }

    @Bean
    public Topic createTopic(){
        return new ActiveMQTopic("java1.bos.topic");
    }


}
