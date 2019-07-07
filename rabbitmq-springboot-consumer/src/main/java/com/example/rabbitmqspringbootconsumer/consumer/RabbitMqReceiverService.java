package com.example.rabbitmqspringbootconsumer.consumer;

import com.example.entity.Order;
import com.rabbitmq.client.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RabbitMqReceiverService {


    /**
     * 进行队列的绑定
     * @param message
     * @param channel
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-rabbitmq-springboot",durable = "true"),
            exchange = @Exchange(value = "exchange-rabbitmq-springboot",durable = "true",type = "topic",ignoreDeclarationExceptions = "true"),
            key = "rabbitmq.#"
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {

        System.out.println("消费端接收到的消息："+message.getPayload());
        //手工ACK
        Long deliverTag = (long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliverTag,false);
    }

    /**
     * 接收java实体消息
     * 可以使用配置文件中的配置，实例：
     * queue-name、queue-durable  在配置文件中配置好就可以使用
     *  @Queue(value = "${queue-name}",durable = "${queue-durable}")
     *
     * @param order
     * @param channel
     * @param header
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-rabbitmq-springboot",durable = "true"),
            exchange = @Exchange(value = "exchange-rabbitmq-springboot",durable = "true",type = "topic",ignoreDeclarationExceptions = "true"),
            key = "rabbitmq.#"
    ))
    @RabbitHandler
    public void onMessage(@Payload Order order, Channel channel, @Headers Map<String,Object> header) throws IOException {

        System.out.println("消费端接收到的消息,java实体："+order.getName());
        //手工ACK
        Long deliverTag = (long)header.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliverTag,false);
    }

}
