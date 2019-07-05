package com.fangzy.rabbitmqJava.customerConsumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 自定义消息处理类
 */
public class MyConsumer extends DefaultConsumer {


    public MyConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body){

        System.out.println("consumerTag:"+consumerTag);
        System.out.println("envelope:"+envelope.getDeliveryTag());
        System.out.println("properties:"+properties);
        System.out.println("body:"+new String(body));
    }
}
