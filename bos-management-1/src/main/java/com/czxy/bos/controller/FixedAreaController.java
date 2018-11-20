package com.czxy.bos.controller;

import com.czxy.bos.domain.base.FixedArea;
import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.bos.service.FixedAreaService;
import com.czxy.bos.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("/fixedArea")
public class FixedAreaController {

    @Autowired
    private FixedAreaService fixedAreaService;

    @PostMapping
    public ResponseEntity<Void> addFixedArea(FixedArea fixedArea){

        try {
            fixedAreaService.addFixedArea(fixedArea);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping
    public ResponseEntity<DataGridResult> findFixedAreaByPage(Integer page, Integer rows){

        try {
            DataGridResult result = fixedAreaService.findFixedAreaByPage(page,rows);
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Autowired
    private RestTemplate restTemplate;

    /**
     * 提供一个方法：返回Customer的数据
     *
     *
     *
     */
    @GetMapping("/noAssociationCustomers")
    public ResponseEntity<String> findNoAssociationCustomers(){
        // 确认请求的URL
        String url = Constants.CRM_MANAGEMENT_HOST +"/customer/noAssociationCustomers";
        // 发起请求,使用RestTemplate发请求
        // getForEntity:发起get请求，并且用第二个参数的类型获取返回的数据
        // 第一个参数：url
        // 第二个参数：使用什么类型接收服务器的返回？
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);


        HttpStatus statusCode = entity.getStatusCode();
        String body = entity.getBody();

        return new ResponseEntity<>(body,statusCode);
    }


    @GetMapping("/hasAssociationFixedAreaCustomers/{id}")
    public ResponseEntity<String> findHasAssociationFixedAreaCustomers(@PathVariable("id") String fixedAreaId){
        // 请求crm服务器
        // 1 拼接url地址
        String url = Constants.CRM_MANAGEMENT_HOST + "/customer/hasAssociationFixedAreaCustomers?fixedAreaId="+fixedAreaId;
        // 2发请求
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);

        HttpStatus statusCode = entity.getStatusCode();
        String body = entity.getBody();

        return new ResponseEntity<>(body,statusCode);

    }

    @GetMapping("/doAssociationFixedAreaCustomers")
    public ResponseEntity<Void> doAssociationFixedAreaCustomers(String idStr,String fixedAreaId){
        System.out.println(idStr);
        System.out.println(fixedAreaId);
        String url = Constants.CRM_MANAGEMENT_HOST + "/customer/doAssociationFixedAreaCustomers?idStr="+idStr+"&fixedAreaId="+fixedAreaId;
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);

        HttpStatus statusCode = entity.getStatusCode();

        return new ResponseEntity<>(statusCode);
    }

    /**
     * 定区关联快递员、快递员关联工作时间
     *
     * @param fixedAreaId
     * @param taketimeId
     * @param courierId
     * @return
     */
    @PostMapping("/assocationCourier")
    public ResponseEntity<Void> assocationCourier(String fixedAreaId,Integer taketimeId,Integer courierId){

        try {
            fixedAreaService.assocationCourier(fixedAreaId,taketimeId,courierId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }




}
