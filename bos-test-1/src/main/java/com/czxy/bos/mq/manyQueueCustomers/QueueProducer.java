package com.czxy.bos.mq.manyQueueCustomers;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 生产者-->生产消息
 *
 *
 */
public class QueueProducer {

    public static void main(String[] args) throws Exception {
        //1 创建链接工厂
        // 括号中可以有参数，当括号中什么参数都没有的时候，默认值是
        // 链接activeMQ的地址：tcp://localhost:61616
        // 账号  admin   密码  admin
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        //2 从链接工厂中获取链接
        Connection connection = connectionFactory.createConnection();
        //3 启动链接
        connection.start();
        //4 获取会话
        // 第一个参数：是否开启事务  true 开启事务，后面一定要提交commit
        // 第二个参数：是否自动确认消息已经被消费
        Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
        //5 创建Queue队列
        Queue queue = session.createQueue("java1.0913");
        //6 创建生产者
        MessageProducer producer = session.createProducer(queue);
       for(int i=0;i<100;i++){
           //7 创建消息
           TextMessage message = session.createTextMessage("Hi,I'm a boy"+i);
           //8 发送消息:生产者发送消息
           producer.send(message);
       }
        //9 提交请求
        session.commit();
        //10 关闭各种资源
        producer.close();
        session.close();
        connection.close();

        System.out.println("OKOKOK");
    }

}
