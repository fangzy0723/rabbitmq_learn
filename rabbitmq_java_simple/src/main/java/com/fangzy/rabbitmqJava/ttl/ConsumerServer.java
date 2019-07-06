package com.fangzy.rabbitmqJava.ttl;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消息过期时间：
 * 分为消息本身有效时间，和队列有效时间
 * 队列有效时间：发送到这个对队列的消息都会有一个统一的有效时间
 * 消息在有效时间内没有被消费就会变成死信
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

        String QUEUE_NAME = "test-queue-ttl";
        String EXCHANGE_NAME = "test-change-ttl";
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
                if((Integer)properties.getHeaders().get("num") == 0){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
