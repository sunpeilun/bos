package com.czxy.fore.controller;

import com.czxy.bos.domain.base.Area;
import com.czxy.bos.domain.take_delivery.Order;
import com.czxy.bos.util.Constants;
import com.czxy.crm.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/foreOrder")
public class ForeOrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpSession session;

    /**
     *
     *
     *
     * @param order
     * @param sendAreaInfo:北京市/北京市/朝阳区---Area信息--解析该信息-找到对应的Area表中的id
     * @param recAreaInfo
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveOrder(Order order, String sendAreaInfo, String recAreaInfo){
        try {
            // 解析sendAreaInfo
            String[] sendAreas = sendAreaInfo.split("/");
            Area sendArea = new Area();
            sendArea.setProvince(sendAreas[0]);
            sendArea.setCity(sendAreas[1]);
            sendArea.setDistrict(sendAreas[2]);

            // 将sendArea设置到order中
            order.setSendArea(sendArea);

            // 解析recAreaInfo
            String[] recAreas = recAreaInfo.split("/");
            Area recArea = new Area();
            recArea.setProvince(recAreas[0]);
            recArea.setCity(recAreas[1]);
            recArea.setDistrict(recAreas[2]);
            // 设置到order中
            order.setRecArea(recArea);

            // 判断客户有没有登录，如果没有登录，直接拒绝请求
            Customer customer =(Customer) session.getAttribute("customer");
            if(customer==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // 给  订单  设置用户id
            order.setCustomer_id(customer.getId());


            // 请求
            String url = Constants.BOS_MANAGEMENT_HOST + "/bosOrder";
            // 发起post请求
            // 第一个参数：url
            // 第二个参数：传递的数据
            // 第三个参数：返回的数据类型
            ResponseEntity<String> entity = restTemplate.postForEntity(url, order, String.class);


            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
