package com.lhever.simpleim.router.basic.http.annotation;

import com.lhever.common.core.consts.CommonConsts;

import java.lang.annotation.*;

@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestParam {

    String name();

    boolean required() default true;

    String defaultValue() default CommonConsts.EMPTY;
}
