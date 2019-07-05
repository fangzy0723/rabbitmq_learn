package com.fangzy.rabbitmqJava.returnListener;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ProcuderServer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("47.52.57.82");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();


        //添加return监听
        //消息发送失败时执行
        channel.addReturnListener((i, s, s1, s2, basicProperties, bytes) -> {
            System.out.println("i:" + i);
            System.out.println("s:" + s);
            System.out.println("s1:" + s1);
            System.out.println("s2:" + s2);
            System.out.println("basicProperties:" + basicProperties);
            System.out.println("bytes:" + bytes);
        });

        String EXCHANGE_NAME = "test-change-return1";
        String BINDING_KEY = "add1";
        String mesg = "hello world";
        //发送消息
        channel.basicPublish(EXCHANGE_NAME, BINDING_KEY, false, false, null, mesg.getBytes());


//
//        channel.close();
//        connection.close();

    }
}
