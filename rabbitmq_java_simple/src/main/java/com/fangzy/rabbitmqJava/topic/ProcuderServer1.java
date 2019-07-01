package com.fangzy.rabbitmqJava.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProcuderServer1 {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.52.57.82");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String EXCHANGE_NAME = "test-change-topic";
        String BINDING_KEY = "update";
        String mesg = "router key 为 update.user的";
        //发送消息
        channel.basicPublish(EXCHANGE_NAME,BINDING_KEY,false,false,null,mesg.getBytes());

        channel.close();
        connection.close();

    }
}
