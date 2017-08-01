package com.laioin.java.frame.kafka.java.api;

import com.laioin.java.frame.kafka.Util;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 16:48
 * <p/>
 * -- kafka 消费者
 */
public class ConsumerMain {

    private static Logger LGR = LoggerFactory.getLogger(ConsumerMain.class);

    public static void main(String[] args) {
        // 创建消费者
        ConsumerConfig config = new ConsumerConfig(Util.getConfByClassPath("/properties/kafka-cons.properties")); // 获取kafka 的配置信息
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
        String topic = "test-k";  // 队列的名称
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));  // 一次取一条数据

        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
        KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
        ConsumerIterator<String, String> it = stream.iterator();
        System.out.println("创建消费者");
        while (it.hasNext()) {   // 如果没有消息，会被阻塞
            MessageAndMetadata<String, String> item = it.next();
            LGR.info("kafka 接受到消息：[{}].", item.message());
            System.out.println("kafka 接受到消息：" + item.message());
            // 处理消息
            //consumerMessage.consumer(item.message());
        }
    }


}
