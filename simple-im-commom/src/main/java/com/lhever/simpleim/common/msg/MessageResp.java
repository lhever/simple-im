package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class MessageResp extends Msg {

    private String id;
    private String sendId;
    private String targetId;
    private String message;
    private Date createTime;


    @Override
    public Integer getType() {
        return MsgType.MESSAGE_RESPONSE;
    }


}
