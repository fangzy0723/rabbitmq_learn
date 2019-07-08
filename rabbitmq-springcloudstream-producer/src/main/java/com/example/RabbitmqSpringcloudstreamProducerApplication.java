package com.example;

import com.example.send.RabbitMqSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RabbitmqSpringcloudstreamProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqSpringcloudstreamProducerApplication.class, args);
    }

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @PostConstruct
    public void sendMsg(){
        Map<String,Object> map = new HashMap<>();
        map.put("send_time",System.currentTimeMillis());
        rabbitMqSender.sendMessage("通过输出管道发送的消息",map);
    }
}
