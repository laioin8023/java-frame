package com.laioin.java.frame.activemq.spring.impl;

import com.laioin.java.frame.activemq.spring.ActivemqDefaultAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 17:35
 */
@Component
public class ActivemqSendMessage {

    @Qualifier("mqAgent")
    @Autowired
    private ActivemqDefaultAgent agent;

    // 发送消息
    public void sendMessage(String msg) {
        agent.send(msg);
    }
}
