package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateGroupResp extends Msg {


    @Override
    public Integer getType() {
        return MsgType.CREATE_GROUP_RESPONSE;
    }

    private Boolean success;

    private String groupId;

    private String createId;

    private List<String> userIds;






}
