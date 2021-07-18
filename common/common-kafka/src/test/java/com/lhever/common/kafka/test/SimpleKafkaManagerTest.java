package com.lhever.common.kafka.test;

import com.lhever.common.kafka.SimpleKafkaManager;

import java.util.Optional;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/16 19:15
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/16 19:15
 * @modify by reason:{方法名}:{原因}
 */
public class SimpleKafkaManagerTest {
    public static void main(String[] args) {

        SimpleKafkaManager simpleKafkaManager = new SimpleKafkaManager("10.33.65.9:9092");
        simpleKafkaManager.listAllTopic();
        String topic = "lihong-topic";
        simpleKafkaManager.getTopic(topic);

        simpleKafkaManager.deleteTopic(topic);
        simpleKafkaManager.createTopic(topic, Optional.of(3), Optional.empty());
    }

}
