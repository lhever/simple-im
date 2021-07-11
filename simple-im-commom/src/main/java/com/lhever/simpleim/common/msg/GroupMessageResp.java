package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;


public class GroupMessageResp extends Msg {

    private String groupId;
    private String fromUserName;
    private String groupMsg;

    @Override
    public Integer getType() {
        return MsgType.GROUP_MESSAGE_RESPONSE;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getGroupMsg() {
        return groupMsg;
    }

    public void setGroupMsg(String groupMsg) {
        this.groupMsg = groupMsg;
    }
}
