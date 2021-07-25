package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageReq extends Msg {

    private String id;
    /**
     * 消息接受者
     */
    private String targetId;

    /**
     * 消息内容
     */
    private String message;


    @Override
    public Integer getType() {
        return MsgType.MESSAGE_REQUEST;
    }
}
