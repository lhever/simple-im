package com.lhever.common.core.exception;

public enum CommonErrorCode {

    SUCCESS("0", "成功"),

    //权限类异常： 比如用户不存在， 密码不正确,
    BUSINESS_EXCEPTION("111101", "业务异常"),

    //redis不可用， pg连不上等情况都属于组件异常
    COMPONENTS_EXCEPTION("111102", "组件异常"),

    CRYPTO_EXCEPTION("111103", "加解密异常"),

    NET_EXCEPTION("200002", "网络错误"),

    FREQUENCY_TOO_HIGH_EXCEPTION("200003", "访问频次过高异常"),


    //参数不合法异常: 比如参数为空/空指针， 除数为0
    ILLEGAL_PARAM_EXCEPTION("000001", "参数有误"),


    //权限类异常： 比如没登陆， 没有携带token,
    AUTHRIZATION_EXCEPTION("000003", "权限异常"),

    NOT_SUPPORTED_EXCEPTION("111104", "不支持的操作异常"),

    UNKNOWN_EXCEPTION("000002", "未知错误");

    private String code;
    private String msg;

    CommonErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static boolean exists(String code) {
       return getByCode(code) != null;
    }

    public static CommonErrorCode getByCode(String code) {
        if (code == null) {
            return null;
        }

        for (CommonErrorCode rspCode : CommonErrorCode.values()) {
            if (code.equals(rspCode.getCode())) {
                return rspCode;
            }
        }
        return null;
    }


}
