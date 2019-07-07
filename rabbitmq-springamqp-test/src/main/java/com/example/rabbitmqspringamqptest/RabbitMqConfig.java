package com.example.rabbitmqspringamqptest;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@ComponentScan({"com.example.rabbitmqspringamqptest"})
public class RabbitMqConfig {



    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("47.52.57.82:5672");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        //AutoStartup必须为true，否则spring容器不会加载RabbitAdmin
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    // spring amqp 声明交换机、队列、绑定的方式如下
    @Bean
    public TopicExchange topicExchange001(){
        return new TopicExchange("topicExchange001", true,false);
    }

    @Bean
    public Queue queue001(){
        return new Queue("queue001",true);
    }

    @Bean
    public Binding binding001(){
        return BindingBuilder.bind(queue001()).to(topicExchange001()).with("spring.*");
    }


    @Bean
    public TopicExchange topicExchange002(){
        return new TopicExchange("topicExchange002", true,false);
    }

    @Bean
    public Queue queue002(){
        return new Queue("queue002",true);
    }

    @Bean
    public Binding binding002(){
        return BindingBuilder.bind(queue002()).to(topicExchange002()).with("rabbit.*");
    }

    @Bean
    public Queue queue003(){
        return new Queue("queue003",true);
    }

    @Bean
    public Binding binding003(){
        return BindingBuilder.bind(queue003()).to(topicExchange002()).with("mq.*");
    }

    @Bean
    public Queue queue_pdf(){
        return new Queue("queue_pdf",true);
    }

    @Bean
    public Queue queue_image(){
        return new Queue("queue_image",true);
    }


    //声明一个RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }


    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(CachingConnectionFactory connectionFactory){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setQueues(queue001(),queue002(),queue003(),queue_image(),queue_pdf());
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setMaxConcurrentConsumers(5);
        simpleMessageListenerContainer.setDefaultRequeueRejected(false);//是否重回队列
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);//自动签收
        //设置消费者标签
        simpleMessageListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return s + "_" + UUID.randomUUID().toString();
            }
        });
        //设置消息监听
        simpleMessageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            //当有新消息时执行这个方法
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                System.out.println("有新消息："+ new String(message.getBody()));
            }
        });

        return simpleMessageListenerContainer;
    }

}
