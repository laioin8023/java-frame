package com.laioin.java.frame.activemq.spring;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 15:07
 * -- activemq，发送接收消息的模式
 */
public enum AckMode {
    AUTO_ACKNOWLEDGE(1), // 指定消息提供者在每次收到消息时自动发送确认。消息只向目标发送一次，但传输过程中可能因为错误而丢失消息。
    CLIENT_ACKNOWLEDGE(2), // 由消息接收者确认收到消息，通过调用消息的acknowledge()方法（会通知消息提供者收到了消息）
    DUPS_OK_ACKNOWLEDGE(3), // 指定消息提供者在消息接收者没有确认发送时重新发送消息（这种确认模式不在乎接收者收到重复的消息）。
    ;
    private int value;

    AckMode(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
