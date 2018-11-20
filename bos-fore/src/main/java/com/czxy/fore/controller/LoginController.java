package com.czxy.fore.controller;

import com.alibaba.fastjson.JSON;
import com.czxy.bos.util.Constants;
import com.czxy.crm.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private HttpSession session;

    @GetMapping
    public ResponseEntity<String> login(String telephone,String password){
        // 根据手机号码去crm系统中查找用户信息
        String url = Constants.CRM_MANAGEMENT_HOST + "/customer/findCustomerByTelephone?telephone="+telephone;
        // 使用对象接收crm系统的返回
        ResponseEntity<Customer> entity = restTemplate.getForEntity(url, Customer.class);

        // 在fore系统中比较密码
        Customer customer = entity.getBody();
        if(customer==null){
            return new ResponseEntity<>("用户名不存在",HttpStatus.NOT_FOUND);
        }
        if(!customer.getPassword().equals(password)){
            return new ResponseEntity<>("密码错误",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 可以保存到session中
        session.setAttribute("customer",customer);
        // 将数据保存到redis中
        redisTemplate.opsForValue().set(telephone,JSON.toJSONString(customer),30, TimeUnit.MINUTES);

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
