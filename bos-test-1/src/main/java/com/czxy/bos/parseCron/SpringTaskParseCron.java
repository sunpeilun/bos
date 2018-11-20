package com.czxy.bos.parseCron;

import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.Date;

public class SpringTaskParseCron {

    public static void main(String[] args) {
        // 使用SpringTask的方式不需要额外导包
        // 相对于哪个时间的下一次执行时间？答：当前系统时间
        Date date = new Date();
        // 表达式
        CronTrigger cronTrigger = new CronTrigger("0 15 10 15 * ?");
        for(int i=0;i<10;i++){
            // 解析表达式的对象
            SimpleTriggerContext context = new SimpleTriggerContext(date,date,date);
            // 获取下一次的执行时间
            date = cronTrigger.nextExecutionTime(context);
            System.out.println(date.toLocaleString());
        }

    }

}