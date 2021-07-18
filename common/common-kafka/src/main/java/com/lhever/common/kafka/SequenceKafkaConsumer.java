package com.lhever.common.kafka;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/15 15:25
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/15 15:25
 * @modify by reason:{方法名}:{原因}
 */

import com.lhever.common.kafka.ack.KafkaAck;
import com.lhever.common.kafka.cfg.ConsumerCfg;
import com.lhever.common.kafka.handler.MsgHandler;
import com.lhever.common.kafka.partition.PartitionRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class SequenceKafkaConsumer<K, V> extends BaseKafkaConsumer<K, V> {

    private ConcurrentHashMap<TopicPartition, PartitionProcessor> partitionRunableMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<TopicPartition, Thread> partitionThreadMap = new ConcurrentHashMap<>();


    public SequenceKafkaConsumer(ConsumerCfg cfg) {
        super(cfg);
    }

    @Override
    protected void init() {
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
        }
        this.kafkaConsumer = new KafkaConsumer(cfg.getProperties());
        if (cfg.topic() != null) {
            kafkaConsumer.subscribe(Collections.singletonList(cfg.topic()), new PartitionRebalanceListener(this));
        } else {
            assignOrSeek(kafkaConsumer, cfg.topicPartitionOffset());
        }
        if (cfg.msgHandler() == null) {
            throw new IllegalArgumentException("no handler");
        }
    }




    @Override
    protected void doRun() {
        ConsumerRecords<K, V> records = kafkaConsumer.poll(cfg.pollDuration());
        for (ConsumerRecord<K, V> record : records) {
            dispatch(record);
        }
    }


    @Override
    protected void doStop() {
        Collection<PartitionProcessor> values = partitionRunableMap.values();
        values.forEach(v -> v.stop());
        partitionRunableMap.clear();
        partitionRunableMap.clear();
    }


    public void dispatch(ConsumerRecord<K, V> record) {
        String topic = record.topic();
        int partition = record.partition();
        long offset = record.offset();
        TopicPartition tp = new TopicPartition(topic, partition);
        PartitionProcessor runable = getProcessor(tp, true);
        runable.submit(record);
    }

    public PartitionProcessor getProcessor(TopicPartition topicPartition, boolean createIfNULL) {
        PartitionProcessor processor = partitionRunableMap.get(topicPartition);
        //如果当前分区还没有开始消费, 则就没有消费任务在map中
        if (processor == null && createIfNULL) {
            processor = create(topicPartition);
        }

        return processor;
    }

    public PartitionProcessor removeProcessor(TopicPartition topicPartition) {
        return partitionRunableMap.remove(topicPartition);
    }

    private synchronized PartitionProcessor create(TopicPartition topicPartition) {
        //生成新的处理任务和线程, 然后将其放入对应的map中进行保存
        PartitionProcessor newProcessor = new PartitionProcessor(this, cfg.msgHandler());

        PartitionProcessor old = partitionRunableMap.putIfAbsent(topicPartition, newProcessor);
        if (old != null) {
            old.stop(); //停止老的线程，使用新的替换
            //发生reblance多次, poller被撤销了分区partition, 又获取到了这个分区的情况
            startProcessor(topicPartition, newProcessor);
        } else {
            startProcessor(topicPartition, newProcessor);
        }

        return newProcessor;
    }

    private void startProcessor(TopicPartition topicPartition, PartitionProcessor processor) {
        partitionRunableMap.put(topicPartition, processor);
        Thread thread = new Thread(processor);
        String name = "processor-partition-" + topicPartition.partition() + "-" + threadCount.getAndAdd(1) ;
        thread.setName(name);
        thread.setDaemon(true);
        System.out.println("start process Thread: " + name);
        partitionThreadMap.put(topicPartition, thread);
        thread.start();
    }





    /**
     * <p>类说明：</p>
     *
     * @author lihong10 2021/7/16 11:24
     * @version v1.0
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2021/7/16 11:24
     * @modify by reason:{方法名}:{原因}
     */
    public static class PartitionProcessor<K,V> implements Runnable {

        private  BlockingQueue<ConsumerRecord<K, V>> records = new LinkedBlockingQueue();
        private SequenceKafkaConsumer sequenceKafkaConsumer;
        private MsgHandler<K, V> msgHandler;
        private volatile boolean isStop = false;

        public PartitionProcessor(SequenceKafkaConsumer sequenceKafkaConsumer, MsgHandler<K, V> msgHandler) {
            this.sequenceKafkaConsumer = sequenceKafkaConsumer;
            this.msgHandler = msgHandler;
        }

        public boolean submit(ConsumerRecord<K, V> record) {
            return records.offer(record);
        }

        public boolean isStop() {
            return isStop;
        }

        public void stop() {
            this.isStop = true;
        }

        @Override
        public void run() {
            while (!isStop) {
                ConsumerRecord<K, V> take = null;
                try {
                    take = records.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (take != null) {
                    handleRecord(take);
                }
            }
        }

        private void handleRecord(ConsumerRecord<K, V> record) {
            try {
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                TopicPartition topicPartition = new TopicPartition(topic, partition);
                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset, null);
                Map<TopicPartition, OffsetAndMetadata> meta = Collections.singletonMap(topicPartition, offsetAndMetadata);
                KafkaAck ack = new KafkaAck(sequenceKafkaConsumer.offsetQueue, meta);
                msgHandler.Handle(record, ack);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }




}

