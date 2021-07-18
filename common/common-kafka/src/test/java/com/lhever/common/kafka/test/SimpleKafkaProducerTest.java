package com.lhever.common.kafka.test;

import com.lhever.common.kafka.SimpleKafkaProducer;
import com.lhever.common.kafka.cfg.ProducerCfg;
import com.lhever.common.kafka.partition.CustomPartitioner;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/16 15:19
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/16 15:19
 * @modify by reason:{方法名}:{原因}
 */
public class SimpleKafkaProducerTest {


    public static void main(String[] args) throws Throwable {
        String topic = "lihong-topic";
        ProducerCfg cfg = new ProducerCfg()
                .bootstrapServers("10.33.65.9:9092")
                .acks("1")
                .retries(3).retryBackoffMs(300)
                .bufferMemory(null).batchSize(null).lingerMs(1000)
                .keySerializer(StringSerializer.class).valueSerializer(StringSerializer.class).partitionerClass(CustomPartitioner.class);

        SimpleKafkaProducer<String, String> simpleKafkaProducer = new SimpleKafkaProducer(cfg);

        String msg = "xxxxsssssssssssssssssssssssss";
        for (int i = 0; i < 10; i++) {

            // 异步回调方式发送消息
            simpleKafkaProducer.send(topic, null, msg, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        System.err.println("发送消息失败:" + exception.getMessage());
                        exception.printStackTrace();
                        return;
                    }
                    if (metadata != null) {
                        System.out.println("异步方式发送消息结果：" + metadata.toString());
                    }
                }
            });
        }

        RecordMetadata result = simpleKafkaProducer.send(topic, null, msg).get();
        System.out.println("同步方式发送消息结果：" + result.toString());

        List<PartitionInfo> partitionInfos = simpleKafkaProducer.partitionsFor(topic);
        System.out.println("分区是：" + partitionInfos);

        simpleKafkaProducer.close();


    }
}
