package com.lhever.common.kafka.ack;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/16 11:31
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/16 11:31
 * @modify by reason:{方法名}:{原因}
 */
public class KafkaAck {

    private final Map<TopicPartition, OffsetAndMetadata> meta;
    private final LinkedBlockingQueue<Map<TopicPartition, OffsetAndMetadata>> offsetQueue;

    public KafkaAck(LinkedBlockingQueue<Map<TopicPartition, OffsetAndMetadata>> offsetQueue, Map<TopicPartition, OffsetAndMetadata> meta) {
        this.offsetQueue = offsetQueue;
        this.meta = meta;
    }

    public void acknowledge() {
        offsetQueue.add(meta);
    }


}
