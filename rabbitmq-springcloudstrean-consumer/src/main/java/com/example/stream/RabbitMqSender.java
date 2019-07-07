package com.example.stream;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@EnableBinding(Barista.class)
public class RabbitMqSender {


    /**
     * 接收消息
     * @param message
     * @throws Exception
     */
    @StreamListener(Barista.INPUTPUT_CHANNEL)
    public void receiverMessage(Message message) throws IOException {
        Channel channel = (com.rabbitmq.client.Channel)message.getHeaders().get(AmqpHeaders.CHANNEL);
        long deliveryTag = (long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("Input stream 接收到的数据："+message);

        channel.basicAck(deliveryTag,false);
    }


}
