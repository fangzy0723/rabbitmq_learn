package com.example.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Barista接口时定义来做i为后面类的参数，定义通道类型和通道名称
 * 通道名称作为配置使用，通道类型则决定了app会使用这一通道进行发送消息还是接收消息
 */
public interface Barista {



    String INPUTPUT_CHANNEL = "inputput_channel";


    /**
     * 注解@Input声明了他是一个输入类型的通道，名字为inputput_channel
     * @return
     */
    @Input(Barista.INPUTPUT_CHANNEL)
    SubscribableChannel loginput();
}
