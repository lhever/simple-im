package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReq extends Msg {

    @Override
    public Integer getType() {
        return MsgType.MESSAGE_REQUEST;
    }

    /**
     * 消息接受者
     */
    private String receiveId;

    /**
     * 消息内容
     */
    private String msg;



}
