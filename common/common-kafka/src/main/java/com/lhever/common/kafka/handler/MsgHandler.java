package com.lhever.common.kafka.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.lhever.common.kafka.ack.KafkaAck;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/15 21:36
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/15 21:36
 * @modify by reason:{方法名}:{原因}
 */
public interface MsgHandler<K, V> {

    public void Handle(ConsumerRecord<K, V> record, KafkaAck ack);

}
