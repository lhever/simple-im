package com.lhever.common.core.utils;

import com.lhever.common.core.support.ognl.DefaultMemberAccess;
import ognl.MemberAccess;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OgnlUtils {
    private final static Logger log = LoggerFactory.getLogger(OgnlUtils.class);

    public static final MemberAccess defaultMemberAccess = new DefaultMemberAccess(true);

    /**
     * 使用OGNL表达式给对象赋值
     *
     * @author lihong10 2018/9/29 14:18:00
     * return
     */
    public static void setValue(String expressiong, Object obj, Object value) {
        if (obj == null) {
            return;
        }
        OgnlContext context = new OgnlContext(null, null, defaultMemberAccess);

        try {
            Ognl.setValue(expressiong, context, obj, value);
        } catch (OgnlException e) {
            if (log.isErrorEnabled()) {
                log.error("对象:{}无字符串类型属性: {}", obj.getClass(), expressiong);
            }
        }

    }


    public static  <T>  T getValue(String expressiong, Object obj) {
        if (obj == null) {
            return null;
        }
        OgnlContext context = new OgnlContext(null, null, defaultMemberAccess);

        try {
            return (T) Ognl.getValue(expressiong, context, obj);
        } catch (OgnlException e) {
            log.error("对象:{}无字符串类型属性: {}", obj.getClass(), expressiong);
        }
        return null;

    }


}
