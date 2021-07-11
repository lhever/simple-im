package com.lhever.common.core.response;

import com.lhever.common.core.exception.CommonErrorCode;
import com.lhever.common.core.utils.JsonUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * * author wangwei
 * * CREATE ON 2017/10/24 11:12
 * * DECRIPTION
 * * WWW.JOINTEM.COM
 **/
@Data
public class CommonResponse<T> implements Cloneable {
    private static Logger log = LoggerFactory.getLogger(CommonResponse.class);

    public static final CommonResponse EMPTY =
            new CommonResponse(CommonErrorCode.SUCCESS.getCode(), CommonErrorCode.SUCCESS.getMsg(), null);


    /**
     * 消息码
     */
    private String code = CommonErrorCode.SUCCESS.getCode();


    /**
     * 消息内容
     */
    private String msg = CommonErrorCode.SUCCESS.getMsg();

    /**
     * 实际数据
     */
    private T data;

    public CommonResponse() {
    }

    public CommonResponse(T t) {
        this.data = t;
    }

    public CommonResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CommonResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String toString() {
        return JsonUtils.object2Json(this);
    }


    @Override
    protected CommonResponse clone() {
        CommonResponse clone = null;
        try {
            clone = (CommonResponse) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("clone对象失败", e);
            clone = new CommonResponse();
        } catch (Throwable e) {
            log.error("clone对象异常", e);
            clone = new CommonResponse();
        }
        return clone;
    }

    public static CommonResponse clone(String code, String msg) {
        CommonResponse clone = EMPTY.clone();
        clone.setCode(code);
        clone.setMsg(msg);
        return clone;
    }


    public static <T> CommonResponse clone(String code, String msg, T data) {
        CommonResponse clone = EMPTY.clone();
        clone.setCode(code);
        clone.setMsg(msg);
        clone.setData(data);
        return clone;
    }

    public static <T> CommonResponse clone(T data) {
        CommonResponse clone = EMPTY.clone();
        clone.setData(data);
        return clone;
    }



}
