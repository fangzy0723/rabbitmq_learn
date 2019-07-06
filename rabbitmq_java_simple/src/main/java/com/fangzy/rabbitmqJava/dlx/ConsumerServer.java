package com.fangzy.rabbitmqJava.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * 死信队列 DLX
 * 利用dlx，当一个消息在一个队列中变成死信（dead message）之后，他会被重新发送到另外一个交换机上，这个交换机就是DLX
 *
 * 消息变成死信的几种情况：
 * 1、消息被拒绝（basic。reject/basic.nack）并且重回队列为false
 * 2、消息TTL过期，在有效时间内没有被消费
 * 3、队列达到最大长度，当队列满了，新送达的消息就会变成死信
 *
 *
 * DLX也是一个正常的Exchange，和一般的Exchange没有区别，他能在任何队列上被指定，实际上就是设置某个队列的属性
 * 当这个队列中有死信时，rabbitmq就会自动把这条消息发送到设置的这个交换机上，进而发送到另外一个私信的队列上
 * 可以监听这个队列中的消息做相应的处理
 *
 *
 * //声明一个死信队列
 * HashMap<String,Object> arguments = new HashMap<>();
 * arguments.put("x-dead-letter-exchange","dlx-exchange");
 *
*  channel.exchangeDeclare("dlx-exchange","topic",true,false,null);
*  channel.queueDeclare("dlx-queue",true,false,false,null);
*  channel.queueBind("dlx-queue","dlx-exchange","#");
 *
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

        String QUEUE_NAME = "test-queue-dlx";
        String EXCHANGE_NAME = "test-change-dlx";
        String EXCHANGE_TYPE = "direct";
        String BINDING_KEY = "add";
        //声明一个交换机  durable：是否持久化
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE,true,false,false,null);
        //声明一个普通的队列
        //设置私信队列,当一个消息转换为死信的时候会被发送到dlx-exchange交换机上
        HashMap<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","dlx-exchange");
        channel.queueDeclare(QUEUE_NAME, false, false, false, arguments);


        //队列和交换机进行绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,BINDING_KEY);


        //声明一个死信队列
        channel.exchangeDeclare("dlx-exchange","topic",true,false,null);
        channel.queueDeclare("dlx-queue",true,false,false,null);
        channel.queueBind("dlx-queue","dlx-exchange","#");

        // 有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel)
        {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                String message = new String(body, "UTF-8");
                System.out.println("新消息： '" + message + "'");


                //成功消费
                channel.basicAck(envelope.getDeliveryTag(),false);

            }
        };
        // 参数：队列名称，是否自动ACK，Consumer
        //消费者手动ack，第二个参数autoAck设置为false
        channel.basicConsume(QUEUE_NAME, false, consumer);

//        channel.close();
//        connection.close();
    }
}
