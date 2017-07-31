package com.laioin.java.frame.activemq.spring;

import com.laioin.java.frame.activemq.exception.ActivemqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 15:08
 * <p/>
 * -- activemq ， 代理 抽象类，
 * 主要是，获取连接
 * 获取对话session
 * 关闭或者获取，生产者
 * 关闭或者获取，消费者
 */
public class AbstractActivemqAgent {
    private static final Logger LGR = LoggerFactory.getLogger(AbstractActivemqAgent.class);

    private ConnectionFactory connFactory;

    public AbstractActivemqAgent(ConnectionFactory connFactory) {
        this.connFactory = connFactory;
    }

    /**
     * 获取，或者创建 activemq 连接
     *
     * @return
     */
    public Connection getConnection() {
        try {
            Connection cnn = connFactory.createConnection();
            cnn.start();
            return cnn;
        } catch (JMSException e) {
            LGR.error("创建 jms 连接错误.", e);
            throw new ActivemqException(e);
        } catch (Throwable t) {
            LGR.error("未知错误，在获取连接的时候.", t);
            throw new ActivemqException(t);
        }
    }

    /**
     * 获取取会话
     *
     * @param cnn             连接
     * @param transacted      是否使用事务:当消息发送者向消息提供者（即消息代理）发送消息时，
     *                        消息发送者等待消息代理的确认，没有回应则抛出异常，消息发送程序负责处理这个错误。
     * @param acknowledgeMode 发送接收方式，参考，AckMode
     * @return
     */
    public Session getSession(Connection cnn, boolean transacted, int acknowledgeMode) {
        try {
            return cnn.createSession(transacted, acknowledgeMode);
        } catch (JMSException e) {
            LGR.error("创建 jms session 错误.", e);
            throw new ActivemqException(e);
        } catch (Throwable t) {
            LGR.error("未知错误，在获取 session的时候..", t);
            throw new ActivemqException(t);
        }
    }

    /**
     * 创建消费者
     *
     * @param ssn  jsm session
     * @param dest 需要处理的 队列
     * @return
     */
    public MessageConsumer createConsumer(Session ssn, Destination dest) {
        try {
            return ssn.createConsumer(dest);
        } catch (JMSException e) {
            LGR.error("创建消费者错误.", e);
            throw new ActivemqException(e);
        } catch (Throwable t) {
            LGR.error("未知错误，在获取消费者的时候.", t);
            throw new ActivemqException(t);
        }
    }

    /**
     * 关闭，消费者
     *
     * @param csm 消费者
     */
    public void closeConsumer(MessageConsumer csm) {
        if (csm != null) {
            try {
                csm.close();
            } catch (JMSException e) {
                LGR.error("关闭消费者错误", e);
                throw new ActivemqException(e);
            } catch (Throwable t) {
                LGR.error("未知错误，在关闭消费者的时候.", t);
                throw new ActivemqException(t);
            }
        }
    }

    /**
     * 创建生产者
     *
     * @param ssn  连接
     * @param dest 需要发送的 队列
     * @return
     */
    public MessageProducer createProducer(Session ssn, Destination dest) {
        try {
            return ssn.createProducer(dest);
        } catch (JMSException e) {
            LGR.error("创建生产者错误.", e);
            throw new ActivemqException(e);
        } catch (Throwable t) {
            LGR.error("未知错误，在创建消费者的时候.", t);
            throw new ActivemqException(t);
        }
    }

    /**
     * 关闭，生产者
     *
     * @param pdc 生产者
     */
    public void closeProducer(MessageProducer pdc) {
        if (pdc != null) {
            try {
                pdc.close();
            } catch (JMSException e) {
                LGR.error("关闭生产者错误.", e);
                throw new ActivemqException(e);
            } catch (Throwable t) {
                LGR.error("未知错误，在关闭生产者的时候.", t);
                throw new ActivemqException(t);
            }
        }
    }

    /**
     * 关闭会话
     *
     * @param ssn 会话
     */
    public void closeSession(Session ssn) {
        if (ssn != null) {
            try {
                ssn.close();
            } catch (JMSException e) {
                LGR.error("关闭会话错误.", e);
                throw new ActivemqException(e);
            } catch (Throwable t) {
                LGR.error("未知错误，在关闭会话的时候..", t);
                throw new ActivemqException(t);
            }
        }
    }

    /**
     * 关闭连接
     *
     * @param cnn 连接
     */
    public void closeConnection(Connection cnn) {
        if (cnn != null) {
            try {
                cnn.close();
            } catch (JMSException e) {
                LGR.error("关闭连接错误.", e);
                throw new ActivemqException(e);
            } catch (Throwable t) {
                LGR.error("未知错误，在关闭连接的时候.", t);
                throw new ActivemqException(t);
            }
        }
    }
}
