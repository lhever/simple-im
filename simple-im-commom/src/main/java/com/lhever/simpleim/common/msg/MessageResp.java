package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class MessageResp extends Msg {

    @Override
    public Integer getType() {
        return MsgType.MESSAGE_RESPONSE;
    }

    private String id;
    private String sendId;
    private String receiveId;
    private String message;
    private Date createTime;





}
