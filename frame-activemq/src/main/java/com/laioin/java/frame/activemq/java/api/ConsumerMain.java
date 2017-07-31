package com.laioin.java.frame.activemq.java.api;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.ByteArrayOutputStream;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 14:46
 * activemq -- 消费者
 */
public class ConsumerMain {
    private static Logger LGR = LoggerFactory.getLogger(ConsumerMain.class);

    private static final String url = "tcp://192.168.1.222:61616";
    private static final String QUEUE_NAME = "topic-name";
    private static final String userName = "name";
    private static final String pwd = "pwd";

    public static void main(String[] args) throws Exception {
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, pwd, url);
        ActiveMQQueue queue = new ActiveMQQueue(QUEUE_NAME);

        Connection cnn = connectionFactory.createConnection();
        cnn.start();

        Session session = cnn.createSession(false, 1);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                String msg = null;
                if (message instanceof TextMessage) {
                    TextMessage tm = (TextMessage) message;
                    try {
                        msg = tm.getText();
                    } catch (JMSException e) {
                        LGR.error("get text from message failed.", e);
                    } catch (Throwable t) {
                        LGR.error("unknown error occured when get text from message.", t);
                    }
                } else if (message instanceof BytesMessage) {
                    try {
                        BytesMessage bm = (BytesMessage) message;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] b = new byte[1024 * 4];
                        for (int c = bm.readBytes(b); c > 0; c = bm.readBytes(b)) {
                            baos.write(b, 0, c);
                        }
                        msg = baos.toString();
                    } catch (Exception e) {
                        LGR.error("failed to convert BytesMessage to String.", e);
                    }
                } else {
                    LGR.warn("unknown type of message received. message:" + message);
                    msg = message.toString();
                }
                System.out.println(msg);
            }
        });

    }
}
