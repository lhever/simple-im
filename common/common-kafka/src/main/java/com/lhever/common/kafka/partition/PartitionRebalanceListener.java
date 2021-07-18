////////////////////////////////////////////////////////////
package com.lhever.common.kafka.partition;

import com.lhever.common.kafka.SequenceKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

/**
 * kafka中的分区发生rebalance的时候，如果poller对应分区被撤销，则用于终止Consumer线程，如果poller获得新的分区，
 * 则也需要从该分区轮询消息
 */
public class PartitionRebalanceListener implements ConsumerRebalanceListener {
    private SequenceKafkaConsumer sequenceKafkaConsumer;

    //初始化方法，传入consumer对象，否则无法调用外部的consumer对象，必须传入
    public PartitionRebalanceListener(SequenceKafkaConsumer sequenceKafkaConsumer) {
        this.sequenceKafkaConsumer = sequenceKafkaConsumer;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {

        //提交偏移量 主要是consumer.commitSync(toCommit); 方法

        sequenceKafkaConsumer.commitOffsetUrgently(true);

        for (TopicPartition partition : collection) {
            System.out.println("onPartitionsRevoked: " + Thread.currentThread().getName() + " 的分区： " + partition.partition() + "被撤销！！！");
            SequenceKafkaConsumer.PartitionProcessor processor = sequenceKafkaConsumer.removeProcessor(partition);
            if (processor != null) {
                processor.stop();
            }
        }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        //rebalance之后 获取新的分区，获取最新的偏移量，设置拉取分量
        for (TopicPartition partition : collection) {

            System.out.printf("onPartitionsAssigned: %s 分配到分区: {%d}", Thread.currentThread().getName(), partition.partition());
        }
    }










}