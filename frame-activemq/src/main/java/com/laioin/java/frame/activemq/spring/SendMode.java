package com.laioin.java.frame.activemq.spring;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 15:07
 * -- 发送消息的是否持久化
 */
public enum SendMode {
    NON_PERSISTENT(1),   // 不持久化
    PERSISTENT(2), // 持久化
    ;

    private int value;

    SendMode(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
