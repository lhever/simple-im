package com.lhever.simpleim.router.basic.cfg;

import com.lhever.common.core.exception.CommonException;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.SequenceKafkaConsumer;
import com.lhever.common.kafka.cfg.ConsumerCfg;
import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.router.service.RouterMsgHandler;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/28 20:44
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/28 20:44
 * @modify by reason:{方法名}:{原因}
 */
@Configuration
public class KafkaConfig {


    @Bean
    public SequenceKafkaConsumer<String, String> init(
            @Qualifier("routerMsgHandler") RouterMsgHandler routerMsgHandler) {
        KafkaUtils.KafkaProp kafkaProp = RouterConfig.kafkaProp;
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
                .msgHandler(routerMsgHandler);
        SequenceKafkaConsumer<String, String> kafkaConsumer = new SequenceKafkaConsumer<>(cfg);

        return kafkaConsumer;


    }


}
