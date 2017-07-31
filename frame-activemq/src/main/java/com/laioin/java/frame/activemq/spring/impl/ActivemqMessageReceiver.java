package com.laioin.java.frame.activemq.spring.impl;

import com.laioin.java.frame.activemq.spring.IMessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 15:21
 * <p/>
 * -- activemq 消费者
 */
@Component
public class ActivemqMessageReceiver implements IMessageReceiver {

    private static Logger LGR = LoggerFactory.getLogger(ActivemqMessageReceiver.class);

    /**
     * 接受消息，并处理
     *
     * @param msg
     */
    @Override
    public void onReceive(String msg) {

    }
}
