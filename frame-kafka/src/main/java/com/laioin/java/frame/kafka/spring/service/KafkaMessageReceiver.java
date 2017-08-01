package com.laioin.java.frame.kafka.spring.service;

import com.laioin.java.frame.kafka.spring.IMessageReceiver;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/1
 * Time on 11:08
 * <p/>
 * --- 具体处理 kafka 消息的service
 */
@Component
public class KafkaMessageReceiver implements IMessageReceiver {

    @Override
    public void onReceive(String msg) {
        System.out.println("--->接收到消息：" + msg);
    }
}
