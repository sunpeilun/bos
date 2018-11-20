package com.czxy.bos.mq.queue;

import io.netty.handler.codec.compression.FastLzFrameEncoder;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消费者
 *
 */
public class QueueConsumer01 {


    public static void main(String[] args) throws  Exception{
        //1 创建链接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        //2 创建链接
        Connection connection = connectionFactory.createConnection();
        //3 启动链接
        connection.start();
        //4 获取会话
        // 第一个参数：是否打开事务 true  打开，后面一定要手动提交
        // 第二个参数：是否自动确认消息被消费
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5 创建队列:当队列名字存在的时候，直接获取该队列连接，当队列名字不存在的时候，自动创建
        Queue queue = session.createQueue("java1.0913");
        //6 创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7 消费消息
        while(true){
            //每10秒钟从activeMQ中读取一次消息
            TextMessage message = (TextMessage) consumer.receive(10000);
            if(message==null){
                break;
            }
            System.out.println(message.getText());

        }
        //8 提交
        //session.commit();
        while (true){

        }
        //9 关闭资源
//        consumer.close();
//        session.close();
//        connection.close();



    }

}
