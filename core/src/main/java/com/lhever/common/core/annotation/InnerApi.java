package com.lhever.common.core.annotation;


import java.lang.annotation.*;

/**
 * 该注解如果添加在controller的方法上（并且也只能添加在Controller的接口方法上），
 * 则表示对应的接口是后门接口，是供排查问题等目的使用的，不是给第三方使用的。
 *
 * @author lihong10 2018年8月13日 上午10:34:51
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerApi {



}
