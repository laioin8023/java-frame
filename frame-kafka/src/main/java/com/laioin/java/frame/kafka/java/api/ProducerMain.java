package com.laioin.java.frame.kafka.java.api;

import com.laioin.java.frame.kafka.Util;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IDEA
 * User qinxiancheng
 * Date on 2017/7/31
 * Time on 16:52
 * -- kafka 生产者
 */
public class ProducerMain {

    public static void main(String[] args) {
        try {
            ProducerConfig config = new ProducerConfig(Util.getConfByClassPath("/properties/kafka-prod.properties"));  // 配置信息
            Producer<String, String> producer = new Producer<String, String>(config);
            List<KeyedMessage<String, String>> data = new ArrayList<KeyedMessage<String, String>>(); // 发送多条消息
            String topic = "test-k"; // 队列的名称
            for (int i = 0; i < 10; i++) {
                String msg = "msgContent";  // 消息内容
                KeyedMessage<String, String> item = new KeyedMessage<>(topic, msg);
                data.add(item);
            }
            long s = System.currentTimeMillis();
            producer.send(data); // 发送消息
            System.out.println("kafka 200K  1000000 ，耗时 ： " + (System.currentTimeMillis() - s));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
