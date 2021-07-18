package com.lhever.common.kafka;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/15 15:16
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/15 15:16
 * @modify by reason:{方法名}:{原因}
 */

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import com.lhever.common.kafka.cfg.ProducerCfg;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class SimpleKafkaProducer<K, V> {

    private ProducerCfg cfg;
    private Producer<K, V> producer;


    public SimpleKafkaProducer(ProducerCfg cfg) {
        this.cfg = cfg;
        this.producer = new KafkaProducer<>(cfg.getProperties());
    }

    public Future<RecordMetadata> send(String topic, K key, V value) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        Future<RecordMetadata> future = producer.send(record);
        return future;
    }

    public Future<RecordMetadata> send(String topic, K key, V value, Callback callback) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        Future<RecordMetadata> future = producer.send(record, callback);
        return future;
    }


    public void flush() {
        producer.flush();
    }


    public List<PartitionInfo> partitionsFor(String topic) {
        return producer.partitionsFor(topic);
    }


    public Map<MetricName, ? extends Metric> metrics() {
        return producer.metrics();
    }


    public void close() {
        producer.close();
    }


    public void close(Duration timeout) {
        producer.close(timeout);
    }




}

