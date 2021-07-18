package com.lhever.common.kafka.cfg;

import com.lhever.common.kafka.handler.MsgHandler;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.time.Duration;
import java.util.Properties;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/15 17:01
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/15 17:01
 * @modify by reason:{方法名}:{原因}
 */
public class ConsumerCfg {

    private Properties properties;

    private String topic;
    private TopicPartitionOffset topicPartitionOffset;
    private Duration pollDuration;
    private MsgHandler msgHandler;
    private Integer concurrency;


    public ConsumerCfg(Properties properties) {
        this.properties = properties;
    }

    public ConsumerCfg() {
        this.properties = new Properties();
    }

    public Properties getProperties() {
        return properties;
    }

    public ConsumerCfg bootstrapServers(String servers) {
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        return this;
    }

    public ConsumerCfg groupId(String groupId) {
        // 消费分组名
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return this;
    }

    public ConsumerCfg enableAutoCommit(boolean autoCommit) {
        // 是否自动提交offset，默认就是true
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(autoCommit));
        return this;
    }

    public ConsumerCfg autoCommitIntervalMs(Integer autoCommitIntervalMs) {
        if (autoCommitIntervalMs == null) {
            autoCommitIntervalMs = 1000;
        }
        // 自动提交offset的间隔时间, 每1000毫秒提交一次
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(autoCommitIntervalMs));
        return this;
    }


    public ConsumerCfg autoOffsetReset(String autoOffsetReset) {
        // 当消费主题的是一个新的消费组，或者指定offset的消费方式，offset不存在，那么应该如何消费
        // latest(默认) ：只消费自己启动之后才发送到主题的消息
        // earliest：第一次从头开始消费，以后按照消费offset记录继续消费，这个需要区别于consumer.seekToBeginning(每次都从头开始消费)
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return this;
    }

    public ConsumerCfg heartbeatIntervalMs(Integer heartbeatIntervalMs) {
        if (heartbeatIntervalMs == null) {
            heartbeatIntervalMs = 1000;
        }
        /**
         *  consumer给broker发送心跳的间隔时间，
         *  broker接收到心跳如果此时有rebalance发生会通过心跳响应将rebalance方案下发给consumer，
         *  这个时间可以稍微短一点
         */
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);
        return this;
    }

    public ConsumerCfg sessionTimeoutMs(Integer sessionTimeoutMs) {
        /**
         * broker接收不到一个consumer的心跳, 持续该时间, 就认为故障了，
         * 会将其踢出消费组，对应的Partition也会被重新分配给其他consumer，默认是10秒
         */
        if (sessionTimeoutMs == null) {
            sessionTimeoutMs = 10 * 1000;
        }
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        return this;
    }

    public ConsumerCfg maxPollRecords(Integer maxPollRecords) {
        if (maxPollRecords == null) {
            maxPollRecords = 100;
        }
        /**
         * 一次poll最大拉取消息的条数，如果消费者处理速度很快，可以设置大点，如果处理速度一般，可以设置小点
         */
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        return this;
    }

    public ConsumerCfg maxPollIntervalMs(Integer maxPollIntervalMs) {
        if (maxPollIntervalMs == null) {
            maxPollIntervalMs = 30 * 1000;
        }
        /**
         * 如果两次poll操作间隔超过了这个时间，broker就会认为这个consumer处理能力太弱，
         * 会将其踢出消费组，将分区分配给别的consumer消费
         */
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        return this;
    }


    public ConsumerCfg keyDeSerializer(Class<?> keyDeSerializer) {
        // 把消息的key从字节数组反序列化为字符串
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeSerializer.getName());
        return this;
    }

    public ConsumerCfg valueDeSerializer(Class<?> valueDeSerializer) {
        // 把消息的value从字节数组反序列化为字符串
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeSerializer.getName());
        return this;
    }


    public ConsumerCfg topic(String topic) {
        if (topicPartitionOffset != null) {
            throw new IllegalArgumentException("topic and topicPartitionOffset are mutal exclusize, you can just assign one of them");
        }
        this.topic = judgeTopic(topic);
        return this;
    }

    public String topic() {
        return topic;
    }

    private String judgeTopic(String topic) {
        if (topic == null || topic.trim().length() == 0) {
            throw new IllegalArgumentException("topic cannot be null");
        }
        return topic.trim();
    }

    public ConsumerCfg topicPartitionOffset(TopicPartitionOffset topicPartitionOffset) {
        if (topic != null) {
            throw new IllegalArgumentException("topic and topicPartitionOffset are mutal exclusize, you can just assign one of them");
        }
        topicPartitionOffset.setTopic(judgeTopic(topicPartitionOffset.getTopic()));
        PartitionOffset[] partitionOffsets = topicPartitionOffset.getPartitionOffsets();
        if (partitionOffsets != null) {
            for (PartitionOffset partitionOffset : partitionOffsets) {
                if (partitionOffset.getPartition() == null) {
                    throw new IllegalArgumentException("partition cann be null if assign");
                }
            }
        }
        this.topicPartitionOffset = topicPartitionOffset;
        return this;
    }

    public TopicPartitionOffset topicPartitionOffset() {
        return topicPartitionOffset;
    }

    public ConsumerCfg pollDuration(Duration pollDuration) {
        this.pollDuration = pollDuration;
        return this;
    }

    public Duration pollDuration() {
      return pollDuration;
    }

    public ConsumerCfg msgHandler(MsgHandler msgHandler) {
        this.msgHandler = msgHandler;
        return this;
    }

    public MsgHandler msgHandler() {
        return msgHandler;
    }

    public ConsumerCfg concurrency(Integer concurrency) {
        this.concurrency = concurrency;
        return this;
    }

    public Integer concurrency() {
        return concurrency;
    }




}
