package com.lhever.simpleim.common;

public class PingPong extends Msg {

    public static final PingPong PING = new PingPong("ping");
    public static final PingPong PONG = new PingPong("pong");

    private String msg;

    public PingPong(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Integer getType() {
        return MsgType.HEART_BEAT;
    }

    @Override
    public String toString() {
        return msg;
    }
}
