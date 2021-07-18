package com.lhever.common.kafka.cfg;

import org.apache.kafka.clients.producer.ProducerConfig;

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
public class ProducerCfg {

    private Properties properties;

    public ProducerCfg(Properties properties) {
        this.properties = properties;
    }

    public ProducerCfg() {
        this.properties = new Properties();
    }

    public ProducerCfg bootstrapServers(String servers) {
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        return this;
    }

    public Properties getProperties() {
        return properties;
    }

    public ProducerCfg acks(String acks) {
         /* 发出消息持久化机制参数
         （1）acks=0： 表示producer不需要等待任何broker确认收到消息的回复，就可以继续发送下一条消息。性能最高，但是最容易丢消息。
         （2）acks=1： 至少要等待leader已经成功将数据写入本地log，但是不需要等待所有follower是否成功写入。就可以继续发送下一条消息。
         这种情况下，如果follower没有成功备份数据，而此时leader又挂掉，则消息会丢失。
         （3）acks=-1或all： 需要等待 min.insync.replicas (默认为1，推荐配置大于等于2) 这个参数配置的副本个数都成功写入日志，
         这种策略会保证只要有一个备份存活就不会丢失数据。这是最强的数据保证。一般除非是金融级别，或跟钱打交道的场景才会使用这种配置。
         如果 min.insync.replicas=1, 则acks=all和acks=1的效果一样
         */
        properties.put(ProducerConfig.ACKS_CONFIG, acks);
        return this;
    }

    public ProducerCfg retries(Integer retries) {
        /**
         *发送失败会重试，默认重试间隔100ms，重试能保证消息发送的可靠性，但是也可能造成消息重复发送，
         * 比如网络抖动，所以需要在接收者那边做好消息接收的幂等性处理
         */
        if (retries == null) {
            retries = 1;
        }
        properties.put(ProducerConfig.RETRIES_CONFIG, retries);
        return this;
    }

    public ProducerCfg retryBackoffMs(Integer retryBackoffMs) {
        // 每次重试之间的间隔
        if (retryBackoffMs == null) {
            retryBackoffMs = 300;
        }
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);
        return this;
    }

    public ProducerCfg bufferMemory(Long bufferMemory) {
        // 设置发送消息的本地缓冲区，如果设置了该缓冲区，消息会先发送到本地缓冲区，可以提高消息发送性能，默认值是33554432，即32MB
        if (bufferMemory == null) {
            bufferMemory = 33554432L;
        }
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        return this;
    }

    public ProducerCfg batchSize(Integer batchSize) {
        if (batchSize == null) {
            batchSize = 16384;
        }
        /**
         * 本地线程会从本地缓冲区取数据，批量发送到broker，设置批量发送消息的大小，
         * 默认值是16384，即16kb，就是说一个batch满了16kb就发送出去
         */
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        return this;
    }

    public ProducerCfg lingerMs(Integer lingerMs) {
        if (lingerMs == null) {
            lingerMs = 10;
        }
      /*   默认值是0，意思就是消息必须立即被发送，但这样会影响性能
         一般设置10毫秒左右，就是说这个消息发送完后会进入本地的一个batch，如果10毫秒内，这个batch满了16kb就会随batch一起被发送出去
         如果10毫秒内，batch没满，那么也必须把消息发送出去，不能让消息的发送延迟时间太长
         */
        properties.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        return this;
    }

    public ProducerCfg keySerializer(Class<?> keySerializer) {
        //发送的key从字符串序列化为字节数组
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer.getName());
        return this;
    }

    public ProducerCfg valueSerializer(Class<?> valueSerializer) {
        // 把发送消息value从字符串序列化为字节数组
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer.getName());
        return this;
    }

    public ProducerCfg partitionerClass(Class<?> partitionerClass) {
        // 分区规则类
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, partitionerClass.getName());
        return this;
    }

    public ProducerCfg deliveryTimeoutMs(Integer deliveryTimeoutMs) {
        // 分区规则类
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
        return this;
    }


}
