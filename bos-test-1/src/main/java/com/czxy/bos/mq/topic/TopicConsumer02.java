package com.czxy.bos.mq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消费者
 *
 */
public class TopicConsumer02 {


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
        Topic topic = session.createTopic("java1.0319");
        //6 创建消费者
        MessageConsumer consumer = session.createConsumer(topic);
        //7 消费消息
        //7 监听器消费消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage tm = (TextMessage)message;
                try {
                    System.out.println(tm.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        // 制造死循环的目的：让监听器一直处于监听状态
        while (true){

        }
        //8 提交
        //session.commit();

        //9 关闭资源
//        consumer.close();
//        session.close();
//        connection.close();



    }

}
