package com.laioin.java.frame.kafka.spring;

import com.laioin.java.frame.kafka.Util;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.message.MessageAndMetadata;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 17:41
 * -- kafka 代理类，装配config，获取或者关闭，生产者消费者, 处理发送和，消费
 */
public class KafkaStringAgent {

    private static Logger LGR = LoggerFactory.getLogger(KafkaStringAgent.class);
    private String topicFlag = "topic.name";
    private Properties configCons; // 消费者配置信息
    private Properties configProd; // 生产者配置信息

    public KafkaStringAgent(String consFile,String prodFile) {
        try {
            // 加载配置文件
            this.configCons = Util.getConfByClassPath(consFile);
            this.configProd = Util.getConfByClassPath(prodFile);
        } catch (Exception e) {
            LGR.error("加载kafka配置文件错误.", e);
        }
    }

    /**
     * 获取，队列名称
     *
     * @return
     */
    public String getTopic(Properties properties) {
        if (null != properties) {
            return (String) properties.get(this.topicFlag);
        }
        return null;
    }

    /**
     * 获取生产者
     *
     * @param properties 配置信息
     * @return
     */
    public Producer getProducer(Properties properties) {
        try {
            ProducerConfig config = new ProducerConfig(properties);
            // 创建生产者
            Producer<String, String> producer = new Producer<String, String>(config);
            return producer;
        } catch (Exception e) {
            LGR.error("kafka 创建生产者错误.", e);
        }
        return null;
    }


    /**
     * 发送消息
     *
     * @param content
     */
    public void send(String content) {
        String topic = this.getTopic(this.configProd); // 获取队列
        KeyedMessage<String, String> item = new KeyedMessage<>(topic, content);
        Producer producer = this.getProducer(this.configProd);
        if (null != producer) {
            producer.send(item); // 发送消息
        }
    }

    /**
     * 发送消息
     *
     * @param contents
     */
    public void send(List<String> contents) {
        String topic = this.getTopic(this.configProd); // 获取队列
        List<KeyedMessage<String, String>> data = new ArrayList<>();
        if (null != contents) {
            for (String itemContent : contents) {
                KeyedMessage<String, String> item = new KeyedMessage<>(topic, itemContent);
                data.add(item);
            }
        }
        Producer producer = this.getProducer(this.configProd);
        if (null != producer) {
            producer.send(data); // 发送消息
        }
    }

    /**
     * 设置处理消息的，service
     *
     * @param receiver
     */
    public void setMessageReceiver(IMessageReceiver receiver) {
        try {
            ConsumerConfig config = new ConsumerConfig(this.configCons);
            ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
            String topic = this.getTopic(this.configCons); // 获取队列
            Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
            topicCountMap.put(topic, new Integer(1));  // 一次取一条数据
            // 编码，解码
            StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
            StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

            Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
            KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
            ConsumerIterator<String, String> it = stream.iterator();
            while (it.hasNext()) {   // 如果没有消息，会被阻塞
                MessageAndMetadata<String, String> item = it.next();
                LGR.info("kafka 接受到消息：[{}].", item.message());
                try {
                    receiver.onReceive(item.message()); // 处理消息
                } catch (Exception e) {
                    LGR.error("处理kafka消息异常.", e);
                }
            }
        } catch (Exception er) {
            LGR.error("创建kafka消费者异常.");
        }
    }

}
