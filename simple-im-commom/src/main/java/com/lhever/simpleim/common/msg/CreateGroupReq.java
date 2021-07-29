package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateGroupReq extends Msg {
    @Override
    public Integer getType() {
        return MsgType.CREATE_GROUP_REQUEST;
    }

    private String createId;

    private String groupName;

    private List<String> userIds;


    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
