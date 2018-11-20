package com.czxy.bos.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class SpringTaskTest02 {

    /**
     * fixedDelay: 当方法执行完成之后，等待6s，再次执行该方法
     * fixedRate: 每隔6s执行一次
     */
    //@Scheduled(fixedRate = 1000)//
    //秒   分时日月 周
    //*/6  * * * * ?  每6s执行一次
    //*/6  8 * * * ?  每小时的第八分钟的每6s执行
    //6    8 * * * ?  每小时的第八分钟第6s执行
    //6,12 8 * * * ?  每小时的第八分钟的第6s和第12s执行
    //6    8 8 * * ?  每天的8点8分6s执行一次
    //6    8 8 8 * ?  每月的8号8点8分6s执行一次
    //                 秒  分时日月 周  年
    @Scheduled(cron = "*/6 * * * * ?")
    public void execute(){
        System.out.println("第二个定时任务"+new Date().toLocaleString());
}


}
