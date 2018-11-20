package com.czxy.bos.scheduling;

import com.czxy.bos.service.take_delivery.PromotionService;
import com.czxy.bos.service.take_delivery.WayBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PromotionJob {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private WayBillService wayBillService;
    /**
     * 每天的夜里0点     盘点
     */
    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron = "0/6 * * * * ?")
    public void execute(){
        System.out.println("正在执行：修改广告状态");
        // 修改promotion表中的数据
        promotionService.changeStatus();


        /**************同步更新elasticsearch索引库**************/
        wayBillService.sysncIndex();

    }

}
