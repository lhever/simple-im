package com.lhever.common.core.exception;

/**
 * <p></p>
 *
 * @author wangzhankai 2020年04月27日 11:39
 * @version V1.0
 */
public class CommonException extends RuntimeException {

    /**错误码**/
    private String errorCode;

    public CommonException(String msg) {
        super(msg);
        this.errorCode = CommonErrorCode.UNKNOWN_EXCEPTION.getCode();
    }

    public CommonException(String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = CommonErrorCode.UNKNOWN_EXCEPTION.getCode();
    }

    public CommonException(Throwable cause) {
        super(cause);
        this.errorCode = CommonErrorCode.UNKNOWN_EXCEPTION.getCode();
    }

    public CommonException(String code, String msg){
        super(msg);
        this.errorCode = code;
    }

    public CommonException(CommonErrorCode errorCode, String msg){
        super(msg);
        this.errorCode = errorCode.getCode();
    }

    public CommonException(CommonErrorCode errorCode){
        super(errorCode.getMsg());
        this.errorCode = errorCode.getCode();
    }


    public CommonException(String code, String msg, Throwable throwable){
        super(msg, throwable);
        this.errorCode = code;
    }

    public CommonException(CommonErrorCode errorCode, String msg, Throwable throwable){
        super(msg, throwable);
        this.errorCode = errorCode.getCode();
    }

    public CommonException(CommonErrorCode errorCode, Throwable throwable){
        super(errorCode.getMsg(), throwable);
        this.errorCode = errorCode.getCode();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }






}
