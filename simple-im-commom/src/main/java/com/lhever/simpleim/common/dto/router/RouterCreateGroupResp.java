package com.lhever.simpleim.common.dto.router;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RouterCreateGroupResp {

    private String groupId;

    private String createId;

    private List<String> userIds;


}
