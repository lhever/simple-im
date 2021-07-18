package com.lhever.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldExportDesc {
    public String display();//导出显示名称

    public int order();//自动显示顺序

    public int width() default 3000;//列宽
}
