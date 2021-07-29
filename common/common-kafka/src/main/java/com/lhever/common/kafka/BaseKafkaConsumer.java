package com.lhever.common.kafka;

import com.lhever.common.kafka.cfg.ConsumerCfg;
import com.lhever.common.kafka.cfg.TopicPartitionOffset;
import com.lhever.common.kafka.util.CommonUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseKafkaConsumer<K, V> extends Thread {

    protected static final AtomicInteger threadCount = new AtomicInteger(1);
    protected final LinkedBlockingQueue<Map<TopicPartition, OffsetAndMetadata>> offsetQueue = new LinkedBlockingQueue<>();
    protected ConsumerCfg cfg;
    protected KafkaConsumer<K, V> kafkaConsumer;
    private volatile boolean runFlag = false;


    public BaseKafkaConsumer(ConsumerCfg cfg) {
        super();
        setName("kafkaConsumer-" + threadCount.getAndAdd(1));
        setDaemon(true);
        this.cfg = cfg;
    }

    public boolean isRuning() {
        return runFlag;
    }

    public synchronized void stopConsumer() {
        if (!runFlag) {
            return;
        }
        this.runFlag = false;
        closeKafkaConsumer();
        doStop();
    }

    private void closeKafkaConsumer() {
        try {
            kafkaConsumer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final synchronized void start() {
        if (runFlag) {
            return;
        }
        this.runFlag = true;
        super.start();
    }


    /**
     * 连续不停的将积压在队列中的偏移量提交完毕
     */
    public void commitOffsetUrgently(boolean commitAll) {
        Map<TopicPartition, OffsetAndMetadata> toCommit = null;
        int count = (commitAll ? Integer.MAX_VALUE : 10000);
        while ((toCommit = offsetQueue.poll()) != null && (toCommit.size() > 0) && count > 0) {

            commitOffset(toCommit, false);
            count--;
        }
    }

    private void commitOffset(Map<TopicPartition, OffsetAndMetadata> commitOffsets, boolean sync) {
        //同步提交offset
        if (commitOffsets == null || commitOffsets.size() == 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        try {
            if (sync) {
                kafkaConsumer.commitSync(commitOffsets);
            } else {
                kafkaConsumer.commitAsync(commitOffsets, new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                        if (exception != null) {
                            exception.printStackTrace();
                            System.out.println(Thread.currentThread().getName() + " 异步提交偏移量报错");
                        }
                    }
                });
            }


            Set<TopicPartition> topicPartitions = commitOffsets.keySet();
            for (TopicPartition topicPartition : topicPartitions) {
                builder.append("(").append(topicPartition.partition()).append(" : ");
                OffsetAndMetadata offsetAndMetadata = commitOffsets.get(topicPartition);
                builder.append(offsetAndMetadata.offset()).append(") ");
            }
            System.out.println(Thread.currentThread().getName() + " 提交偏移量完毕: " + builder.toString());
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " 提交偏移量报错！！！" + builder.toString());
        }
    }


    @Override
    public void run() {
        init();
        while (runFlag) {
            commitOffsetUrgently(true);
            try {
                doRun();
            } catch (Throwable e) {

            }
        }
    }


    protected void init() {
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
        }
        this.kafkaConsumer = new KafkaConsumer(cfg.getProperties());
        if (CommonUtils.isNotEmpty(cfg.topics())) {
            kafkaConsumer.subscribe(cfg.topics());
        } else {
            assignOrSeek(kafkaConsumer, cfg.topicPartitionOffsets());
        }
        if (cfg.msgHandler() == null) {
            throw new IllegalArgumentException("no handler");
        }
    }

    protected void assignOrSeek(KafkaConsumer<K, V> kafkaConsumer, Collection<TopicPartitionOffset> topicPartitionOffsets) {

        Map<TopicPartition, Long> topicPartitionOffsetMap = new HashMap<>();
        List<TopicPartition> tps = new ArrayList<>();
        for (TopicPartitionOffset tpo : topicPartitionOffsets) {
            TopicPartition topicPartition = new TopicPartition(tpo.getTopic(), tpo.getPartition());
            tps.add(topicPartition);

            if (tpo.getOffset() != null) {
                topicPartitionOffsetMap.put(topicPartition, tpo.getOffset());
            }
        }
        kafkaConsumer.assign(tps);
        Set<Map.Entry<TopicPartition, Long>> entries = topicPartitionOffsetMap.entrySet();
        for (Map.Entry<TopicPartition, Long> entry : entries) {
            kafkaConsumer.seek(entry.getKey(), entry.getValue());
        }
    }

    protected abstract void doRun();

    protected abstract void doStop();


}


