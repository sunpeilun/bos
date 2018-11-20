package com.czxy.fore.controller;

import com.czxy.bos.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;

/**
 * 前台的促销的控制器名字
 */
@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 请求bos系统，获取广告分页数据
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/findPromotionByPage")
    public ResponseEntity<String> findPromotionByPage(Integer page,Integer rows){
        // 准备URL
        String url = Constants.BOS_MANAGEMENT_HOST + "/promotion?page="+page+"&rows="+rows;
        // 发送请求
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        HttpStatus statusCode = entity.getStatusCode();
        String body = entity.getBody();
        return new ResponseEntity<>(body,statusCode);
    }


}
