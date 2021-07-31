package com.lhever.simpleim.common.dto.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/31 21:12
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/31 21:12
 * @modify by reason:{方法名}:{原因}
 */
@Getter
@Setter
@NoArgsConstructor
public class KafkaSingleGroupMessage {

    private String sendId;
    private String groupId;
    private String receiveId;
    private String groupMsg;

}
