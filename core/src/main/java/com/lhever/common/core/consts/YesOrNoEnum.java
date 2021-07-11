package com.lhever.common.core.consts;

public enum YesOrNoEnum {

    YES(1, "是", "yes"),

    NO(0,  "否", "no"),

    UNKNOWN(-1, "未知", "unknown");

    private Integer code;
    private String zhMsg;
    private String enMsg;

    YesOrNoEnum(Integer code, String zhMsg, String enMsg){
        this.code = code;
        this.zhMsg = zhMsg;
        this.enMsg = enMsg;
    }

    public Integer getCode() {
        return code;
    }

    public String getZhMsg() {
        return zhMsg;
    }

    public String getEnMsg() {
        return enMsg;
    }
}
