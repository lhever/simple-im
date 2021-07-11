package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class loginResp extends Msg {

    private boolean success;
    private String userId;
    private String userName;


    @Override
    public Integer getType() {
        return MsgType.LOGIN_RESP;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
