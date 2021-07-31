package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class GroupMessageReq extends Msg {

    @Override
    public Integer getType() {
        return MsgType.GROUP_MESSAGE_REQUEST;
    }

    private String sendId;
    private String groupId;
    private String groupMsg;



}
