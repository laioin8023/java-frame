package com.laioin.java.frame.kafka.spring.service;

import com.laioin.java.frame.kafka.spring.KafkaStringAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/8/1
 * Time on 11:10
 * -- kafka 发送者
 */
@Component
public class KafkaSendMessage {

    @Autowired
    @Qualifier("kafkaAgent")
    private KafkaStringAgent agent;

    public void sendMessage(String content) {
        agent.send(content); // 发送消息
    }
}
