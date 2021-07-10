package com.lhever.simpleim.common;

public class AuthResp extends Msg {

    private Boolean success;
    private String clientId;


    @Override
    public Integer getType() {
        return MsgType.LOGIN_RESP;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
