package com.czxy.bos.mq.manyQueueCustomers;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 采用监听器消费消息
 */
public class QueueConsumer02 {

    public static void main(String[] args) throws Exception {
        //1 创建连接工厂
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        //2 创建连接
        Connection connection = connectionFactory.createConnection();
        //3 启动连接
        connection.start();
        //4 获取会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5 创建队列
        Queue queue = session.createQueue("java1.0913");
        //6 创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7 监听器消费消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage tm = (TextMessage)message;
                try {
                    System.out.println("消费者2："+tm.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        // 制造死循环的目的：让监听器一直处于监听状态
        while (true){

        }

        //8 提交

        //9 关闭资源


    }

}
