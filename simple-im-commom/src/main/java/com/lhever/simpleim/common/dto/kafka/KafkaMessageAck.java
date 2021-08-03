package com.lhever.simpleim.common.dto.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/8/3 21:35
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/8/3 21:35
 * @modify by reason:{方法名}:{原因}
 */
@Getter
@Setter
@NoArgsConstructor
public class KafkaMessageAck {
    private String msgId;
    private String receiveId;
}
