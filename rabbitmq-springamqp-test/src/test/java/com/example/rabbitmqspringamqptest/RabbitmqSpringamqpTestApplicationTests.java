package com.example.rabbitmqspringamqptest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringamqpTestApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void springAmqpTest(){
        //声明交换机
        rabbitAdmin.declareExchange(new DirectExchange("test_springAmqp_direct_exchange",false,false));
        rabbitAdmin.declareExchange(new FanoutExchange("test_springAmqp_fanout_exchange",false,false));
        rabbitAdmin.declareExchange(new TopicExchange("test_springAmqp_topic_exchange",false,false));

        //声明队列
        rabbitAdmin.declareQueue(new Queue("test_springAmqp_direct_queue",false,false,false,null));
        rabbitAdmin.declareQueue(new Queue("test_springAmqp_fanout_queue",false,false,false,null));
        rabbitAdmin.declareQueue(new Queue("test_springAmqp_topic_queue",false,false,false,null));

        //声明绑定
        //队列和交换机必须提前声明
        rabbitAdmin.declareBinding(new Binding("test_springAmqp_direct_queue",
                Binding.DestinationType.QUEUE,
                "test_springAmqp_direct_exchange",
                "add",new HashMap<>()));
        //声明绑定
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test_springAmqp_direct_queue"))
                .to(new DirectExchange("test_springAmqp_direct_exchange")).with("direct"));



        //清空队列中的数据
        rabbitAdmin.purgeQueue("test_springAmqp_direct_queue",false);

    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage001(){
        //创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc","消息描述");
        messageProperties.getHeaders().put("type","自定义消息类型");
        Message message = new Message("这是一个发送的消息".getBytes(),messageProperties);

        //发送消息
        rabbitTemplate.convertAndSend("topicExchange001", "spring.amqp", message, new MessagePostProcessor() {
            //发送消息时会对发送的消息再次进行加工，完后发送
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("消息发送额外添加的配置信息");
                message.getMessageProperties().getHeaders().put("desc","额外修改的信息描述");
                message.getMessageProperties().getHeaders().put("attr","额外添加的信息属性");
                return message;
            }
        });
    }


    @Test
    public void testSendMessage002(){
        //创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        Message message = new Message("这是一个发送的文本消息".getBytes(),messageProperties);

        //发送消息
        rabbitTemplate.convertAndSend("topicExchange001", "spring.amqp", message);
        rabbitTemplate.convertAndSend("topicExchange002", "rabbit.amqp","这是发送的消息内容");
    }


}
