package com.lhever.simpleim.router.basic.http.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface RestController {

}