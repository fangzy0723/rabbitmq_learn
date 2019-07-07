package com.example.send;


import com.example.stream.Barista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@EnableBinding(Barista.class)
public class RabbitMqSender {


    @Autowired
    private Barista barista;

    /**
     * 发送消息
     * @param message
     * @param properties
     * @throws Exception
     */
    public void sendMessage(Object message, Map<String,Object> properties){
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message,messageHeaders);

        boolean sendStatus = barista.logout().send(msg);
        System.out.println("发送的消息："+message+",发送的状态："+sendStatus);
    }


}
