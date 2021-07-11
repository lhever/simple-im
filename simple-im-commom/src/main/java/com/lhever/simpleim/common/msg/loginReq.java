package com.lhever.simpleim.common.msg;

import com.lhever.simpleim.common.consts.MsgType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class loginReq extends Msg {

    private String userName;
    private String pwd;

    public loginReq() {
    }

    public loginReq(String userName, String pwd) {
        this.userName = userName;
        this.pwd = pwd;
    }

    @Override
    public Integer getType() {
        return MsgType.LOGIN_REQ;
    }


}
