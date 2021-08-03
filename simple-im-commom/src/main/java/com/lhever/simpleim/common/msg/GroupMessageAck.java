package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class GroupMessageAck extends Msg{

    @Override
    public Integer getType() {
        return MsgType.GROUP_MESSAGE_ACK;
    }

    private String groupId;
    private String groupMsgId;

    private String receiveId;
    private String userGroupMsgId;



}
