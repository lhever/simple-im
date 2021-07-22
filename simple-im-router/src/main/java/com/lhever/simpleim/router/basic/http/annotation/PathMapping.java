package com.lhever.simpleim.router.basic.http.annotation;


import java.lang.annotation.*;

@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PathMapping {

    String uri() default "";

    HttpMethod method() default HttpMethod.GET;
}
