package com.czxy.bos.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class SpringTaskTest01 {


    /**
     * 何时执行某个方法？
     * 定时任务执行的方法的定义有什么要求？
     * 答：公共无参无返回值，方法名随意
     *
     */
    @Scheduled(fixedDelay = 6000)//每6s执行一次
    public void execute(){

        System.out.println("当前时间是:"+new Date().toLocaleString());

    }
}
