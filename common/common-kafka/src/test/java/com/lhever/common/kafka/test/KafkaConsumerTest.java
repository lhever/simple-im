package com.lhever.common.kafka.test;

import com.lhever.common.kafka.SequenceKafkaConsumer;
import com.lhever.common.kafka.ack.KafkaAck;
import com.lhever.common.kafka.cfg.ConsumerCfg;
import com.lhever.common.kafka.cfg.PartitionOffset;
import com.lhever.common.kafka.cfg.TopicPartitionOffset;
import com.lhever.common.kafka.handler.MsgHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/16 15:17
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/16 15:17
 * @modify by reason:{方法名}:{原因}
 */
public class KafkaConsumerTest {

    public static void main(String[] args) throws InterruptedException {

        final String TOPIC = "lihong-topic";
        ConsumerCfg cfg = new ConsumerCfg()
                .bootstrapServers("127.0.0.1:9092")
                .groupId("group-id-0")
                .enableAutoCommit(false)
                .autoOffsetReset("earliest")
                .heartbeatIntervalMs(10000)
                .sessionTimeoutMs(10 * 10000)
                .maxPollRecords(100)
                .maxPollIntervalMs(5 * 10000)
                .keyDeSerializer(StringDeserializer.class)
                .valueDeSerializer(StringDeserializer.class)
                .topicPartitionOffset(Arrays.asList(new TopicPartitionOffset(TOPIC, 0, 0L), new TopicPartitionOffset(TOPIC, 1, 0L),
                        new TopicPartitionOffset(TOPIC, 2, 0L)))
//                .topics(Arrays.asList(TOPIC))
                .pollDuration(Duration.ofMillis(300))
                .concurrency(3)
                .msgHandler( new MsgHandler<String, String>() {
                    @Override
                    public void Handle(ConsumerRecord<String, String> record, KafkaAck ack) {
                        try {
                            System.out.printf(Thread.currentThread().getName() + "收到消息：topic= %s, partition = %d, offset = %d, key = %s, value = %s%n",
                                    record.topic(), record.partition(), record.offset(), record.key(), record.value());
                        } finally {
                            ack.acknowledge();
                        }
                    }
                });

//        ConcurrentKafkaConsumer<String, String> consumer = new ConcurrentKafkaConsumer<>(cfg);
        SequenceKafkaConsumer<String, String> consumer = new SequenceKafkaConsumer<>(cfg);
        consumer.start();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }



    public void test() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(new Properties());

        String TOPIC = "topic-test";

        // 订阅主题
        consumer.subscribe(Collections.singletonList(TOPIC));

        // 消费指定分区, 订阅主题:subscribe和分配分区:assign, 只需要一个就好, assign里已经包含了topic的信息了
//		consumer.assign(Collections.singletonList(new TopicPartition(TOPIC, 0)));
//		consumer.assign(Collections.singletonList(new TopicPartition(TOPIC, 1)));

        // 消息回溯消费(每次都从log的最开始消费)
//		consumer.assign(Collections.singletonList(new TopicPartition(TOPIC, 0)));
//		consumer.seekToBeginning(Collections.singletonList(new TopicPartition(TOPIC, 0)));

        // 指定offset起消费
//		consumer.assign(Collections.singletonList(new TopicPartition(TOPIC, 0)));
//		consumer.seek(new TopicPartition(TOPIC, 0), 37);

        // 从指定时间点开始消费
        List<PartitionInfo> topicPartitions = consumer.partitionsFor(TOPIC);
        // 从3小时前开始消费
        long fetchBeginAt = System.currentTimeMillis() - 1000 * 60 * 60 * 3;
        // 确认每个分区的开始消费时刻
        Map<TopicPartition, Long> topicPartitionBeginAtMap = new HashMap<>();
        for (PartitionInfo partition : topicPartitions) {
            topicPartitionBeginAtMap.put(new TopicPartition(TOPIC, partition.partition()), fetchBeginAt);
        }
        // 根据每个分区的开始消费时刻找到对应时刻的offset和timestamp
        Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap = consumer.offsetsForTimes(topicPartitionBeginAtMap);
        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : topicPartitionOffsetAndTimestampMap.entrySet()) {
            // 轮流消费每个分区的指定起始offset的消息
            TopicPartition key = entry.getKey();
            OffsetAndTimestamp value = entry.getValue();
            if (key == null || value == null) continue;
            long offset = value.offset();
            System.out.println("partition-" + key.partition() + "|offset-" + offset);
            System.out.println();
            // 根据消费里的timestamp确定offset
            consumer.assign(Collections.singletonList(key));
            consumer.seek(key, offset);
            // 然后开始消费, 先不写了, 和下面的消费流程一样
        }

        while (true) {
            // poll(duration): 长轮询, 即duration时段内没拿到消息就一直重复尝试拿, 知道时间到或者拿到消息才返回结果
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf(Thread.currentThread().getName() + " 收到消息：partition = %d,offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
            }

            if (records.count() > 0) {
                // 手动同步提交offset，当前线程会阻塞直到offset提交成功, 一般使用同步提交，因为提交之后一般也没有什么逻辑代码了
                consumer.commitSync();

                // 手动异步提交offset，当前线程提交offset不会阻塞，可以继续处理后面的程序逻辑
				/*consumer.commitAsync(new OffsetCommitCallback() {
					@Override
					public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
						if (exception != null) {
							System.err.println("Commit failed for " + offsets);
							System.err.println("Commit failed exception: " + Arrays.toString(exception.getStackTrace()));
						}
					}
				});*/
            }
        }
    }
}
