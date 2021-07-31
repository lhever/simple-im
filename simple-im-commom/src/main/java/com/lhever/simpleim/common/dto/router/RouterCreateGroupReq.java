package com.lhever.simpleim.common.dto.router;

import com.lhever.simpleim.common.consts.MsgType;
import com.lhever.simpleim.common.msg.Msg;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RouterCreateGroupReq {

    private String createId;

    private String groupName;

    private List<String> userIds;
}
