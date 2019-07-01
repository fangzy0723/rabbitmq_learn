package com.fangzy.rabbitmqJava.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

        String EXCHANGE_NAME = "test-change-fanout";
        for (int i = 0; i < 100 ; i++) {
            String mesg = "hello world"+ i;
            //发送消息,fanout不需要bindkey，有也会被忽略
            channel.basicPublish(EXCHANGE_NAME,"",false,false,null,mesg.getBytes());
        }

        channel.close();
        connection.close();

    }
}
