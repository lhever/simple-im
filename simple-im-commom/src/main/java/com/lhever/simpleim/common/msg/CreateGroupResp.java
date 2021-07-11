package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;

import java.util.List;


public class CreateGroupResp extends Msg {

    private Boolean success;

    private String groupId;

    private List<String> userIds;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }


    @Override
    public Integer getType() {
        return MsgType.CREATE_GROUP_RESPONSE;
    }
}
