package com.lhever.common.kafka;

import com.lhever.common.kafka.ack.KafkaAck;
import com.lhever.common.kafka.cfg.ConsumerCfg;
import com.lhever.common.kafka.handler.MsgHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public class ConcurrentKafkaConsumer<K, V> extends BaseKafkaConsumer<K, V> {

    private ExecutorService executor;

    public ConcurrentKafkaConsumer(ConsumerCfg cfg) {
        super(cfg);
    }


    protected void init() {
        super.init();
        Integer concurrency = cfg.concurrency();
        if (executor != null) {
            executor.shutdown();
        }
        executor = Executors.newFixedThreadPool(concurrency == null ? 1 : concurrency, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("simple-kafka-msg-handler-" + threadCount.getAndAdd(1));
                thread.setDaemon(true);
                return thread;
            }
        });
    }


    @Override
    protected void doRun() {
        ConsumerRecords<K, V> records = kafkaConsumer.poll(cfg.pollDuration());
        for (ConsumerRecord<K, V> record : records) {
            Processor processor = new Processor(this, record, cfg.msgHandler());
            executor.submit(processor);
        }
    }


    @Override
    protected void doStop() {
        executor.shutdown();
        executor = null;
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
    public static class Processor<K, V> implements Runnable {

        private ConcurrentKafkaConsumer kafkaExecutorConsumer;
        private ConsumerRecord<K, V> record;
        private MsgHandler<K, V> msgHandler;

        public Processor(ConcurrentKafkaConsumer kafkaExecutorConsumer, ConsumerRecord<K, V> record, MsgHandler<K, V> msgHandler) {
            this.kafkaExecutorConsumer = kafkaExecutorConsumer;
            this.record = record;
            this.msgHandler = msgHandler;
        }

        @Override
        public void run() {
            try {
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                TopicPartition topicPartition = new TopicPartition(topic, partition);
                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset, null);
                Map<TopicPartition, OffsetAndMetadata> meta = Collections.singletonMap(topicPartition, offsetAndMetadata);
                KafkaAck ack = new KafkaAck(kafkaExecutorConsumer.offsetQueue, meta);
                msgHandler.Handle(record, ack);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


}


