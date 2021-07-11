package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;

import java.util.List;


public class CreateGroupReq extends Msg {
    @Override
    public Integer getType() {
        return MsgType.CREATE_GROUP_REQUEST;
    }

    private List<String> userIds;


    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
