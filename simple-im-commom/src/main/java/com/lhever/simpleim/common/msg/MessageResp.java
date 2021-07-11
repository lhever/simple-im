package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;


public class MessageResp extends Msg {

    /**
     * 响应内容
     */
    private String message;

    /**
     * 消息来源
     */
    private String fromUserId;
    private String fromUserName;


    @Override
    public Integer getType() {
        return MsgType.MESSAGE_RESPONSE;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
