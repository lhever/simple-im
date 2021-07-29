package com.lhever.simpleim.server;

import com.lhever.common.core.exception.CommonException;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.SequenceKafkaConsumer;
import com.lhever.common.kafka.cfg.ConsumerCfg;
import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.common.util.RedisUtils;
import com.lhever.simpleim.server.config.ServerConfig;
import com.lhever.simpleim.server.support.kafkaMsgHandler;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/28 21:34
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/28 21:34
 * @modify by reason:{方法名}:{原因}
 */
public class ServerInitializer {

    private static SequenceKafkaConsumer<String, String> kafkaConsumer;

    public static void init() {
        initRedis();
        initKafkaProducer();
        initKafkaConsumer();
    }

    private static void initRedis() {
        RedisUtils.RedisProp redisProp = new RedisUtils.RedisProp();
        redisProp.setHost(ServerConfig.REDIS_IP);
        redisProp.setPort(ServerConfig.REDIS_PORT);
        redisProp.setPass(ServerConfig.REDIS_PWD);
        redisProp.setTimeout(20000);
        redisProp.setMaxIdle(5);
        redisProp.setMaxTotal(20);
        redisProp.setMaxWaitMillis(20000);
        redisProp.setTestOnBorrow(true);
        RedisUtils.init(redisProp);
    }

    private static void initKafkaProducer() {
        KafkaUtils.init(ServerConfig.KAFKA_ADDRESS);
    }

    public static void initKafkaConsumer() {
        KafkaUtils.KafkaProp kafkaProp = ServerConfig.kafkaProp;
        if (kafkaProp == null) {
            throw new CommonException("no kafka config");
        }
        List<String> topics = StringUtils.splitToList(kafkaProp.getTopics(), ",");
        ConsumerCfg cfg = new ConsumerCfg()
                .bootstrapServers(kafkaProp.getAddress())
                .groupId(kafkaProp.getGroupId())
                .enableAutoCommit(false)
                .autoOffsetReset(kafkaProp.getOffset())
                .heartbeatIntervalMs(10000)
                .sessionTimeoutMs(10 * 10000)
                .maxPollRecords(100)
                .maxPollIntervalMs(5 * 10000)
                .keyDeSerializer(StringDeserializer.class)
                .valueDeSerializer(StringDeserializer.class)
                .topics(topics)
                .pollDuration(Duration.ofMillis(1000))
                .concurrency(3)
                .msgHandler(new kafkaMsgHandler());
        kafkaConsumer = new SequenceKafkaConsumer<>(cfg);
        kafkaConsumer.start();
    }


}
