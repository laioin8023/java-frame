package com.laioin.java.frame.activemq.spring;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 15:07
 * -- activemq 处理消息的接口
 */
public interface IMessageReceiver {

    /**
     * 处理消息的方法
     *
     * @param msg 消息内容
     */
    void onReceive(String msg);
}
