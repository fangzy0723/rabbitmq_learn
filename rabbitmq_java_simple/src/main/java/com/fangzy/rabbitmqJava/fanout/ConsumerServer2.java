package com.fangzy.rabbitmqJava.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;


public class ConsumerServer2 {
    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.52.57.82");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        //是否自动重连
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //每隔多久重连一次
        connectionFactory.setNetworkRecoveryInterval(3000);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String QUEUE_NAME = "test-queue-fanout";
        String EXCHANGE_NAME = "test-change-fanout";
        String EXCHANGE_TYPE = "fanout";
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE);
        //声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //队列和交换机进行绑定,fanout不需要bindkey，有也会被忽略
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");

        // 有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                String message = new String(body, "UTF-8");
                System.out.println("ConsumerServer2新消息： '" + message + "'");
            }
        };
        //消费消息，自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(QUEUE_NAME, true, consumer);

//        channel.close();
//        connection.close();
    }
}
