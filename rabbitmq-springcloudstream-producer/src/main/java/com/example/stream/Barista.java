package com.example.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Barista接口时定义来做i为后面类的参数，定义通道类型和通道名称
 * 通道名称作为配置使用，通道类型则决定了app会使用这一通道进行发送消息还是接收消息
 */
public interface Barista {

    String OUTPUT_CHANNEL = "output_channel";


    /**
     * 发送消息用输出通道
     * 注解@Output声明了他是一个输出类型的通道，名字为output_channel
     * @return
     */
    @Output(Barista.OUTPUT_CHANNEL)
    MessageChannel logout();


}
