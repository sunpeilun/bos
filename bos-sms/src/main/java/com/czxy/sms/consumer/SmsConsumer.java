package com.czxy.sms.consumer;

import com.czxy.sms.util.SmsUtil;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;

@Component
public class SmsConsumer {

    @JmsListener(destination = "java1.bos.sms")
    public void recive(Message message){
      try {
          MapMessage mapMessage  = (MapMessage)message;
          String telephone = mapMessage.getString("telephone");
          String code = mapMessage.getString("code");
          System.out.println(telephone+":"+code);
          // 发送短信，调用SMSutil
          // SmsUtil.sendSms(telephone,code);

      }catch (Exception e){

      }

    }

}
