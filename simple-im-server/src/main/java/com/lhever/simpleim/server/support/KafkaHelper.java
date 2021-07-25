package com.lhever.simpleim.server.support;

import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.SimpleKafkaProducer;
import com.lhever.common.kafka.cfg.ProducerCfg;
import com.lhever.common.kafka.partition.CustomPartitioner;
import com.lhever.simpleim.server.config.ServerConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaHelper {
    private static Logger logger = LoggerFactory.getLogger(KafkaHelper.class);

    private static String TOPIC_IN ;
    private static String TOPIC_OUT ;
    private static SimpleKafkaProducer<String, String> simpleKafkaProducer;

    static {
        init();
    }

    public static  void init() {

        TOPIC_IN = StringUtils.appendAll(ServerConfig.SERVER_IP, "-", ServerConfig.SERVER_PORT, "-", "in");
        TOPIC_OUT = StringUtils.appendAll(ServerConfig.SERVER_IP, "-", ServerConfig.SERVER_PORT, "-", "out");

        ProducerCfg cfg = new ProducerCfg()
                .bootstrapServers(ServerConfig.KAFKA_ADDRESS)
                .acks("1")
                .retries(3).retryBackoffMs(3000)
                .bufferMemory(null).batchSize(null).lingerMs(3000)
                .keySerializer(StringSerializer.class).valueSerializer(StringSerializer.class).partitionerClass(CustomPartitioner.class);
        simpleKafkaProducer = new SimpleKafkaProducer(cfg);
    }

    public static void sendMessage(Object obj) {
        if (obj == null) {
            return;
        }
        String msg = obj.toString();
        if (StringUtils.isBlank(msg)) {
            return;
        }
        // 异步回调方式发送消息
        simpleKafkaProducer.send(TOPIC_OUT, null, msg, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    logger.info("发送消息到:{}失败, 原因:{}", TOPIC_OUT, exception.getMessage());
                    return;
                }
                if (metadata != null) {
                    logger.info("发送消息到:{}, 详情:{}", TOPIC_OUT, metadata.toString());
                }
            }
        });
    }







}
