package com.czxy.fore.controller;

import com.alibaba.fastjson.JSON;
import com.czxy.bos.util.Constants;
import com.czxy.bos.util.GetRandomCodeUtil;
import com.czxy.bos.util.MailUtil;
import com.czxy.crm.domain.Customer;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.jms.Queue;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/regist")
public class RegistController {

    @Autowired
    private HttpSession session;


    @Autowired
    private Queue queue;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @GetMapping("/sendSms")
    public ResponseEntity<Void> sendSms(String telephone){
        // 随机验证码
        String code = GetRandomCodeUtil.getCode();
        // 将验证码保留---保留到session中（redis中）
        session.setAttribute(telephone,code);

        // 发送短信
//        try {
//            SmsUtil.sendSms(telephone,code);
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }

        /***********采用MQ发送短信***********/
        // 需要发送的数据：telephone +  code
        try {
            //准备消息
            ActiveMQMapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("telephone",telephone);
            mapMessage.setString("code",code);
            //发送消息
            jmsMessagingTemplate.convertAndSend(queue,mapMessage);

        }catch (Exception e){

        }


        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Autowired
    private RestTemplate restTemplate;


    //redis模板
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping
    public ResponseEntity<Void> regist(String checkcode,Customer customer){

        // 获取验证码
        String code = session.getAttribute(customer.getTelephone()).toString();
        if(code!=null&&!"".equals(code.trim())){
            if (checkcode.equals(code)){
                // 拼接url
                String url = Constants.CRM_MANAGEMENT_HOST + "/customer/saveCustomer";
                // 发送请求
                // 第一个参数：url
                // 第二个参数：保存的对象
                // 第三个参数：返回值类型
                ResponseEntity<String> entity = restTemplate.postForEntity(url, customer, String.class);

                // 发送注册邮件 +
                // 邮件内容：激活账号
                // 激活链接地址  + 手机号  +  激活code
                // href= localhost:8092/regist/activeMail?telephone=xxx&activecode=xxxxxx
                // 根据手机号码去redis中查找激活码进行激活
                /************************/
                // 准备激活码    当值中间有-（比如：aasdd-sadfadsf） 时，此时是无法传递的
                String activeCode = UUID.randomUUID().toString().replace("-","");
                // 保存到redis
                // 第一个：key
                // 第二个：value
                // 第三个：时长
                // 第四个：单位
                redisTemplate.opsForValue().set(customer.getTelephone(),activeCode,24,TimeUnit.HOURS);
                // 拼接链接地址
                String activeUrl = Constants.FORE_MANAGEMENT_HOST + "/regist/activeMail?telephone="+customer.getTelephone()+"&activeCode="+activeCode;
                String content = "<a href='"+activeUrl+"'>速运快递账号激活</a>";
                // 发送邮件
                try {
                    MailUtil.sendMail(customer.getEmail(),"世纪佳缘网账号激活",content);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                HttpStatus statusCode = entity.getStatusCode();
                return new ResponseEntity<>(statusCode);

            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/activeMail")
    public ResponseEntity<Void> activeMail(String telephone,String activeCode){
        // 1 根据手机号到redis中查找code，
        String redisCode = redisTemplate.opsForValue().get(telephone);
        // 1.1 找不到，则直接返回无法验证
        if(redisCode==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // 1.2 找到了
        if(redisCode.equals(activeCode)){
            // 删除激活码
            redisTemplate.delete(telephone);

            // 需要激活用户
            // 2 根据telephone查找用户信息
            String url = Constants.CRM_MANAGEMENT_HOST +"/customer/findCustomerByTelephone?telephone="+telephone;
            ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
            String body = entity.getBody();
            System.out.println(body);
            // json格式的字符串，放的是customer的信息，这个串能否转成Customer对象？
            // 答：fastjson？阿里巴巴出品
            // 第一个参数：json串
            // 第二个参数：要转成的类型
            Customer customer = JSON.parseObject(body, Customer.class);
            if(customer.getType()!=null&&customer.getType()==1){
                System.out.println("已经激活，无需激活");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            // 需要激活--修改customer的数据
            // 2.1 查到，判断用户是否已经激活，已激活不需要重复激活
            // 2.2 没查找到用户，直接返回无法验证
            // 3 未激活，进行激活操作，并且返回结果
            String updateUrl = Constants.CRM_MANAGEMENT_HOST + "/customer/updateType?telephone="+telephone;
            ResponseEntity<String> updateEntity = restTemplate.getForEntity(updateUrl,String.class);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        return null;
    }





}
