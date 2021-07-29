package com.lhever.common.kafka.cfg;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/16 10:33
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/16 10:33
 * @modify by reason:{方法名}:{原因}
 */
@Getter
@Setter
@NoArgsConstructor
public class TopicPartitionOffset {
    private String topic;
    private Integer partition;
    private Long offset;

    public TopicPartitionOffset(String topic, Integer partition, Long offset) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }
}
