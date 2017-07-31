package com.laioin.java.frame.activemq.java.api;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 14:46
 * activemq -- 生产者
 */
public class ProducerMain {
    private static final String url = "tcp://192.168.1.222:61616";
    private static final String QUEUE_NAME = "topic-name";
    private static final String userName = "name";
    private static final String pwd = "pwd";

    public static void sendMessage() throws Exception {
        try {
            // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, pwd, url);
            ActiveMQQueue queue = new ActiveMQQueue(QUEUE_NAME);

            Connection cnn = connectionFactory.createConnection();
            cnn.start();

            Session session = cnn.createSession(false, 1);
            int size = 1000;
            long s = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                String msg = "大多数消息系统声称可以做到“精确的一次”，但是仔细阅读它们的的文档可以看到里面存在误导，比如没有说明当consumer或producer失败时怎么样，或#" + i;
                TextMessage tm = session.createTextMessage(msg);
                MessageProducer pdc = session.createProducer(queue);
                pdc.send(tm, 2, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
            }
            System.out.println("send 200K size " + size + "  " + (System.currentTimeMillis() - s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ProducerMain.sendMessage();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
