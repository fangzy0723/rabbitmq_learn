package com.fangzy.rabbitmqJava.ttl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;

public class ProcuderServer {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.52.57.82");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String EXCHANGE_NAME = "test-change-ttl";
        String BINDING_KEY = "add";
        for (int i = 0; i < 10 ; i++) {
            String mesg = "hello world"+ i;

            HashMap<String,Object> map = new HashMap<>();
            map.put("num",i);
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)//发送消息使用持久化模式
                    .contentEncoding("UTF-8")
                    .headers(map)
                    .expiration("1000")//设置消息的有效时间
                    .build();

            //发送消息
            channel.basicPublish(EXCHANGE_NAME,BINDING_KEY,false,false,basicProperties,mesg.getBytes());
        }

        channel.close();
        connection.close();

    }
}
