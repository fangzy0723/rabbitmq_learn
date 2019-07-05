package com.fangzy.rabbitmqJava.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;


public class ConsumerServer {
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

        String QUEUE_NAME = "test-queue-confirm";
        String EXCHANGE_NAME = "test-change-confirm";
        String EXCHANGE_TYPE = "direct";
        String BINDING_KEY = "add";
        //声明一个交换机  durable：是否持久化
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE,true,false,false,null);
        //声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //队列和交换机进行绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,BINDING_KEY);

        // 有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                String message = new String(body, "UTF-8");
                System.out.println("新消息： '" + message + "'");
            }
        };
        // 参数：队列名称，是否自动ACK，Consumer
        channel.basicConsume(QUEUE_NAME, true, consumer);

//        channel.close();
//        connection.close();
    }
}
