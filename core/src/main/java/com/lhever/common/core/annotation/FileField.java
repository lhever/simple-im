package com.lhever.common.core.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用于解析使用的对象字段注解
 *
 * @Author: wulang
 * @Date: 2017年10月09日 16:48
 * @Version: v1.0
 * @Description:
 * @Modified By:
 * @Modifued reason
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface FileField {
    /**
     * 字段名
     *
     * @return
     * @since 1.0
     */
    String name() default "";

    /**
     * 字段顺序
     */
    int index() default 0;

    /**
     * 默认值
     *
     * @return
     * @since 1.0
     */
    String defaultValue() default "";


    /**
     * 转换之后的目标类型
     *
     * @return
     * @since 1.0
     */
    Class<?> newType() default Object.class;


    /**
     * 提示信息
     * @return
     */
    String tips() default "";

    /**
     * 校验规则配置
     * @return
     */
    String validateRegx() default "";

    /**
     * 是否必填
     * @return
     */
    boolean required() default false;

    /**
     * 忽略字段：控制Tips扩展
     * @return
     */
    boolean ignore() default false;


}

