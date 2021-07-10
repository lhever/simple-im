package com.lhever.simpleim.common;

public class AuthReq extends Msg {

    private String user;
    private String pwd;

    public AuthReq() {
    }

    public AuthReq(String user, String pwd) {
        this.user = user;
        this.pwd = pwd;
    }

    @Override
    public Integer getType() {
        return MsgType.LOGIN_REQ;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
