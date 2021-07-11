package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;


public class MessageReq extends Msg {
    /**
     * 消息接受者
     */
    private String targetUserId;

    /**
     * 消息内容
     */
    private String message;


    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Integer getType() {
        return MsgType.MESSAGE_REQUEST;
    }
}
