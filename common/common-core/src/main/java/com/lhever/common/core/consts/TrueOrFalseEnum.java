package com.lhever.common.core.consts;

public enum TrueOrFalseEnum {
    TRUE(true, "真", "true"),
    FALSE(false,  "假", "false");
    private boolean value;
    private String zhMsg;
    private String enMsg;

    TrueOrFalseEnum(boolean value, String zhMsg, String enMsg){
        this.value = value;
        this.zhMsg = zhMsg;
        this.enMsg = enMsg;
    }

    public boolean getValue() {
        return value;
    }

    public String getZhMsg() {
        return zhMsg;
    }

    public String getEnMsg() {
        return enMsg;
    }
}
