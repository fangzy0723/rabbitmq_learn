package com.fangzy.rabbitmqJava.qos;

import com.rabbitmq.client.*;

import java.io.IOException;


/**
 * 消费端限流
 * prefetchSize：0
 * prefetchCount：告诉rabbitmq不要同时给消费者推送多于N条消息，即一旦有N条消息没有ACK则该consumer将block掉，直到有消息ACK
 * 消费者最多有N条没有ACK的消息，在no_ack=false（手动应答）的情况下生效
 * global：true/false是否将上面设置应用于channel，，指的是限制channel级别还是consumer级别
 * true：channel  false：consumer
 */

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

        String QUEUE_NAME = "test-queue-qos";
        String EXCHANGE_NAME = "test-change-qos";
        String EXCHANGE_TYPE = "direct";
        String BINDING_KEY = "add";
        //声明一个交换机  durable：是否持久化
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE,true,false,false,null);
        //声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //队列和交换机进行绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,BINDING_KEY);

        ///1、 1：每次只处理一条消息，
//             false：consumer级别限制
        channel.basicQos(0,1,false);


        // 有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                String message = new String(body, "UTF-8");
                System.out.println("新消息： '" + message + "'");
                //3、手动ack签收,告诉消息服务器消息已经消费了
                //第一个参数：消息的唯一索引
                //第二个参数：是否批量.true:将一次性ack所有小于deliveryTag的消息。
                channel.basicAck(envelope.getDeliveryTag(),true);
            }
        };
        // 参数：队列名称，是否自动ACK，Consumer
        // 2、消费限流 auto_ack必须为false
        channel.basicConsume(QUEUE_NAME, false, consumer);

//        channel.close();
//        connection.close();
    }
}
