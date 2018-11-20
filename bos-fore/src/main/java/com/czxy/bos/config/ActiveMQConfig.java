package com.czxy.bos.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class ActiveMQConfig {

    @Bean
    public Queue createQueue(){
        return new ActiveMQQueue("java1.bos.sms");
    }

}
