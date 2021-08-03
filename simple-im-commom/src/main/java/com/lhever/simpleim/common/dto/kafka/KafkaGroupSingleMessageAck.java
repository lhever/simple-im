package com.lhever.simpleim.common.dto.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class KafkaGroupSingleMessageAck {


    private String groupId;
    private String groupMsgId;

    private String receiveId;
    private String userGroupMsgId;



}
