package com.lhever.simpleim.common;

public abstract class Msg {

    private byte version = 1;

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public abstract Integer getType();

}
