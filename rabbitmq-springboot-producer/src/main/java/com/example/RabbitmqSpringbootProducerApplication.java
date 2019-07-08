package com.example;

import com.example.entity.Order;
import com.example.rabbitmqspringbootproducer.producer.RabbitMqSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RabbitmqSpringbootProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqSpringbootProducerApplication.class, args);
    }
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 服务启动后立即执行
     * 测试消息的发送
     */
    @PostConstruct
    public void testSpringBootSendMessage(){
        Map<String,Object> properties = new HashMap<>();
        properties.put("num","1234567890");
        properties.put("send_time",System.currentTimeMillis());

        rabbitMqSendService.sendMessage("这是使用spring boot发送的消息",properties);

    }

    /**
     * 服务启动后立即执行
     * 测试消息的发送
     * 发送java对象消息
     */
//    @PostConstruct
    public void testSpringBootSendEntityMessage(){
        Map<String,Object> properties = new HashMap<>();
        properties.put("type","java entity");
        properties.put("send_time",System.currentTimeMillis());

        Order order = new Order();
        order.setId(1);
        order.setName("fangzy");
        rabbitMqSendService.sendMessage(order,properties);

    }
}
