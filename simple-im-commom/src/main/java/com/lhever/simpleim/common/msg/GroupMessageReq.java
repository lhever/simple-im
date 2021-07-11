package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;


public class GroupMessageReq extends Msg {

    private String groupId;
    private String groupMsg;

    @Override
    public Integer getType() {
        return MsgType.GROUP_MESSAGE_REQUEST;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupMsg() {
        return groupMsg;
    }

    public void setGroupMsg(String groupMsg) {
        this.groupMsg = groupMsg;
    }
}
