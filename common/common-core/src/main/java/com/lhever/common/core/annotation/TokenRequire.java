package com.lhever.common.core.annotation;


import java.lang.annotation.*;

/**
 * 该注解如果添加在controller的方法上（并且也只能添加在Controller的接口方法上），则对应方法的返回值会被自动拦截并组装成ApiResult对象。
 *
 * @author lihong10 2018年8月13日 上午10:34:51
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenRequire {
}
