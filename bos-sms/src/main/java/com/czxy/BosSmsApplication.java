package com.czxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BosSmsApplication {

    public static void main(String[] args) {
        System.out.println("Sms消费者启动了....");
        SpringApplication.run(BosSmsApplication.class, args);
    }
}
