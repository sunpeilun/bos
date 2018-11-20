package com.czxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 开启定时任务调度框架
public class BosManagement1Application {

    public static void main(String[] args) {
        SpringApplication.run(BosManagement1Application.class, args);
    }
}
