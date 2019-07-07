package com.example.rabbitmqspringbootproducer.producer;


import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * publisher-confirms 实现一个监听器用于监听broker端给我们返回确认请求：RabbitTemplate.ConfirmCallback
 * publisher-returns 保证消息对broker端是可达的，如果出现路由键不可达的情况，则使用监听器对不可达的消息进行后续的处理
 * 保证消息的路由成功 ：RabbitTemplate.ReturnCallback
 * 注意：在发生消息的时候对template进行配置mandatory=true保证监听有效
 * 生产端还可以配置其他属性，比如发送重试、超时时间、次数、间隔等
 *
 */

@Component
public class RabbitMqSendService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息发送成功的回调
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {

        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.out.println("correlationData:"+correlationData);
            System.out.println("ack:"+b);
            System.out.println("返回信息:"+s);
            //b  true:发送成功  flase：发送失败
            if(!b){
                System.out.println("在此进行异常处理");
            }else{
                System.out.println("发送成功");
            }

        }
    };
    /**
     * 根据路由键消息不可达执行这个回调，可以在这个回调用进行消息重试
     */
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        /**
         * @param message  消息实体
         * @param i  响应码
         * @param s  响应信息
         * @param s1 交换机
         * @param s2  路由键
         */
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int i, String s, String s1, String s2) {

            System.out.println("message:"+new String(message.getBody())+"，replayCode:"+i+"，replyText:"+s+"，exchange:"+s1+"，exchange:"+s2);
        }
    };

    public void sendMessage(Object message, Map<String,Object> properties){
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message,messageHeaders);

        /**
         * 使用回调，通道不能关闭
         */
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());//消息id全局唯一,ack保证消息唯一使用的这个值
        rabbitTemplate.convertAndSend("exchange-rabbitmq-springboot","rabbitmq1.springboot",msg,correlationData);


    }
}
