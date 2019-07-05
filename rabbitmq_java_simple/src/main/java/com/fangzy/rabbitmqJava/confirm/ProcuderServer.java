package com.fangzy.rabbitmqJava.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;

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

        //指定确认模式
        channel.confirmSelect();

        String EXCHANGE_NAME = "test-change-confirm";
        String BINDING_KEY = "add1";
        String mesg = "hello world";
        //发送消息
        channel.basicPublish(EXCHANGE_NAME,BINDING_KEY,false,false,null,mesg.getBytes());

        //添加confirm监听
        channel.addConfirmListener(new ConfirmListener() {
            //消息发送成功时执行
            //l 消息唯一的标签
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("-----ack---------" + l);
            }

            //返回失败时执行
            //l 消息唯一的标签
            //磁盘写满、mq异常、队列到达上限  走nack
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("-----nack---------" + l);
            }
        });

//
//        channel.close();
//        connection.close();

    }
}
