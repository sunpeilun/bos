package com.czxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *SrpingBoot整合activeMQ
 * queue队列模式
 *     1 生产者--发送消息*
 *     2 消费者--消费消息
 *
 * topic广播模式
 *     1 生产者--发送消息
 *     2 消费者--消费消息
 *
 */
@SpringBootApplication
public class BosMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(BosMqApplication.class, args);
    }
}
