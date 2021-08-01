package com.lhever.simpleim.common.util;

import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.SimpleKafkaProducer;
import com.lhever.common.kafka.cfg.ProducerCfg;
import com.lhever.common.kafka.partition.CustomPartitioner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class KafkaUtils {
    private static Logger logger = LoggerFactory.getLogger(KafkaUtils.class);

    public static String ROUTER_TOPIC_PREFIX = "im-router-topic-";
    public static int ROUTER_TOPIC_TOTAL = 20;

    public static String SERVER_TOPIC_TPL = "server-{}-";

    public static int SERVER_TOPIC_TOTAL = 2;


    private static SimpleKafkaProducer<String, String> simpleKafkaProducer;

    public static volatile List<String> ROUTER_TOPICS;

    static {
        List<String> topics = new ArrayList();
        for (int i = 0; i < KafkaUtils.ROUTER_TOPIC_TOTAL; i++) {
            String topic = KafkaUtils.ROUTER_TOPIC_PREFIX + i;
            topics.add(topic);
        }
        ROUTER_TOPICS = topics;
    }





    @Getter
    @Setter
    @NoArgsConstructor
    public static class KafkaProp {
        private String address;
        private String groupId;
        private String offset;
        private String topics;
    }


    public static void init(String address) {
        ProducerCfg cfg = new ProducerCfg()
                .bootstrapServers(address)
                .acks("1")
                .retries(3).retryBackoffMs(3000)
                .bufferMemory(null).batchSize(null).lingerMs(3000)
                .keySerializer(StringSerializer.class).valueSerializer(StringSerializer.class).partitionerClass(CustomPartitioner.class);
        simpleKafkaProducer = new SimpleKafkaProducer(cfg);
    }

    private static void sendMessage(String topic, String msg) {
        if (StringUtils.isBlank(msg)) {
            return;
        }
        // 异步回调方式发送消息
        simpleKafkaProducer.send(topic, null, msg, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    logger.info("发送消息到:{}失败, 原因:{}", topic, exception.getMessage());
                    return;
                }
                if (metadata != null) {
                    logger.info("发送消息到:{}, 详情:{}", topic, metadata.toString());
                }
            }
        });
    }

    public static void sendToRouter(int hash, String prefix, Object obj) {
        if (obj == null) {
            return;
        }
        String topic = getTopic(hash, ROUTER_TOPIC_TOTAL, ROUTER_TOPIC_PREFIX);
        sendMessage(topic, prefix + JsonUtils.obj2Json(obj));
    }

    public static void sendToServer(int hash, String serverTopicPrefix,  String msgPrefix,  Object obj) {
        if (obj == null) {
            return;
        }
        String topic = getTopic(hash, SERVER_TOPIC_TOTAL, serverTopicPrefix);
        sendMessage(topic, msgPrefix + JsonUtils.obj2Json(obj));
    }


    private static String getTopic(int hash, int total, String topicPrefix) {
        int i = hash % total;
        String topic = topicPrefix + i;
        return topic;
    }


}
