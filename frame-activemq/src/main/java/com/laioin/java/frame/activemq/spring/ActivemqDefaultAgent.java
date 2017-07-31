package com.laioin.java.frame.activemq.spring;

import com.laioin.java.frame.activemq.exception.ActivemqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.ByteArrayOutputStream;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 15:13
 * -- activemq 默认的 代理，
 * 处理，接收和发送消息
 */
public class ActivemqDefaultAgent extends AbstractActivemqAgent {

    private static final Logger LGR = LoggerFactory.getLogger(ActivemqDefaultAgent.class);

    private boolean defaultTransacted;  // 是否使用事务:当消息发送者向消息提供者（即消息代理）发送消息时，消息发送者等待消息代理的确认，没有回应则抛出异常，消息发送程序负责处理这个错误。
    private AckMode defaultAckMode; //  消息发送接收时的方式，参考 ackmode
    private SendMode defaultSendMode; // 是否持久化，参考 sendmode
    private Destination defaultDestination; // 处理的队列

    public ActivemqDefaultAgent(ConnectionFactory connFactory,
                                Destination defaultDestination) {
        super(connFactory);
        this.defaultTransacted = false;
        this.defaultAckMode = AckMode.AUTO_ACKNOWLEDGE;
        this.defaultSendMode = SendMode.PERSISTENT;
        this.defaultDestination = defaultDestination;
    }

    public ActivemqDefaultAgent(ConnectionFactory connFactory,
                                Destination defaultDestination,
                                boolean defaultTransacted, AckMode defaultAckMode,
                                SendMode defaultSendMode) {
        super(connFactory);
        this.defaultTransacted = defaultTransacted;
        this.defaultAckMode = defaultAckMode;
        this.defaultSendMode = defaultSendMode;
        this.defaultDestination = defaultDestination;
    }

    /**
     * 发送文字消息
     *
     * @param msg 消息内容
     */
    public void send(String msg) {
        this.send(defaultTransacted, defaultAckMode, defaultDestination, defaultSendMode, msg);
    }

    /**
     * 发送消息
     *
     * @param msg      消息内容
     * @param sendMode 是否持久化
     */
    public void send(String msg, SendMode sendMode) {
        this.send(defaultTransacted, defaultAckMode, defaultDestination, sendMode, msg);
    }

    /**
     * 发送消息
     *
     * @param transacted 是否使用事务
     * @param ackMode    发送接收消息的模式
     * @param dest       处理的对象
     * @param sendMode   是否持久化
     * @param msg        消息内容
     */
    public void send(boolean transacted, AckMode ackMode, final Destination dest,
                     final SendMode sendMode, final String msg) {
        try {
            Connection cnn = getConnection();
            Session ssn = getSession(cnn, transacted, ackMode.value());
            try {
                TextMessage tm = ssn.createTextMessage(msg);
                MessageProducer pdc = ssn.createProducer(dest);
                pdc.send(tm, sendMode.value(), Message.DEFAULT_PRIORITY,
                        Message.DEFAULT_TIME_TO_LIVE);
            } finally {
                closeSession(ssn);
                closeConnection(cnn);
            }
        } catch (JMSException e) {
            LGR.error("发送消息错误.", e);
            throw new ActivemqException(e);
        } catch (Throwable t) {
            LGR.error("未知错误，在发送消息:" + msg + "的时候.", t);
            throw new ActivemqException(t);
        }
    }

    /**
     * 接收消息
     *
     * @param transacted  是否使用事务
     * @param ackMode     发送接收消息的模式
     * @param dest        处理的队列
     * @param msgReceiver 具体处理消息的实现类
     * @param msgSelector 选择器
     */
    public void setMessageReceiver(boolean transacted, AckMode ackMode, final Destination dest,
                                   final IMessageReceiver msgReceiver, final String msgSelector) {
        try {
            Connection cnn = getConnection();
            Session ssn = getSession(cnn, transacted, ackMode.value());
            MessageConsumer consumer = ssn.createConsumer(dest);
            consumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message message) {
                    String msg = null;
                    if (message instanceof TextMessage) {
                        TextMessage tm = (TextMessage) message;
                        try {
                            msg = tm.getText();
                        } catch (JMSException e) {
                            LGR.error("获取 TextMessage 消息错误.", e);
                            throw new ActivemqException(e);
                        } catch (Throwable t) {
                            LGR.error("未知错误，在获取 TextMessage 消息的时候.", t);
                            throw new ActivemqException(t);
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
                            LGR.error("获取 BytesMessage 时错误.", e);
                        }
                    } else {
                        LGR.warn("未知错误，在获取 BytesMessage 的时候.:" + message);
                        msg = message.toString();
                    }
                    try {
                        msgReceiver.onReceive(msg);
                    } catch (Exception e) {
                        LGR.error("在处理消息的时候错误，消息:" + msg, e);
                    }
                }
            });
        } catch (JMSException e) {
            LGR.error("赋值jsm listener 时错误.", e);
            throw new ActivemqException(e);
        } catch (Throwable t) {
            LGR.error("未知错误，在赋值jsm listener 的时候.", t);
            throw new ActivemqException(t);
        }
    }

    /**
     * 设置，处理消息的service
     *
     * @param transacted  是否使用事务
     * @param ackMode     发送接收消息的模式
     * @param dest        处理的队列
     * @param msgReceiver 处理消息的实现类
     */
    public void setMessageReceiver(boolean transacted, AckMode ackMode, final Destination dest,
                                   final IMessageReceiver msgReceiver) {
        setMessageReceiver(transacted, ackMode, dest, msgReceiver, null);
    }

    public void setMessageReceiver(final Destination dest, final IMessageReceiver msgReceiver) {
        setMessageReceiver(this.defaultTransacted, this.defaultAckMode, dest, msgReceiver, null);
    }

    /**
     * 设置，处理消息的service
     *
     * @param msgReceiver
     */
    public void setMessageReceiver(final IMessageReceiver msgReceiver) {
        setMessageReceiver(this.defaultTransacted, this.defaultAckMode, this.defaultDestination,
                msgReceiver, null);
    }

    /**
     * 是否使用事务
     *
     * @return
     */
    public boolean getDefaultTransacted() {
        return defaultTransacted;
    }

    /**
     * 获取介绍和处理消息的类型
     *
     * @return
     */
    public AckMode getDefaultAckMode() {
        return defaultAckMode;
    }

    /**
     * 是否持久化
     *
     * @return
     */
    public SendMode getDefaultSendMode() {
        return defaultSendMode;
    }

    /**
     * 处理的队列
     *
     * @return
     */
    public Destination getDefaultDestination() {
        return defaultDestination;
    }
}
