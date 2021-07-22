package com.lhever.simpleim.router.basic.listener;

import com.lhever.simpleim.router.basic.http.annotation.RestController;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ControllerScanner {

    private ApplicationContext ctx;

    private List<Class<?>> controllerClass;

    public ControllerScanner(ApplicationContext ctx) {
        this.ctx = ctx;
        this.controllerClass = loadControllerClass(ctx);
    }


    private List<Class<?>> loadControllerClass(ApplicationContext ctx) {
        final Class<? extends Annotation> clazz = RestController.class;
        return ctx.getBeansWithAnnotation(clazz)
                .values().stream()
                .map(AopUtils::getTargetClass)
                .filter(cls -> Objects.nonNull(cls.getAnnotation(clazz)))
                .collect(Collectors.toList());
    }

    public List<Class<?>> loadControllerClass() {
        return controllerClass;
    }


}
