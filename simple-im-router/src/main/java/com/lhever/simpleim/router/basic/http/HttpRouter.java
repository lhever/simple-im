package com.lhever.simpleim.router.basic.http;

import com.google.common.collect.Maps;
import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.utils.FileUtils;
import com.lhever.simpleim.router.basic.http.annotation.PathMapping;
import com.lhever.simpleim.router.basic.http.annotation.RestController;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class HttpRouter {

    private static final Logger logger = LoggerFactory.getLogger(HttpRouter.class);

    private Map<Request, RequestHandler> httpRouterMapper = Maps.newConcurrentMap();
    private Map<String, Object> controllerBeans = Maps.newConcurrentMap();


    public void loadControllerClass(ApplicationContext ctx) {
        final Class<? extends Annotation> clazz = RestController.class;
        ctx.getBeansWithAnnotation(clazz)
                .values().stream().forEach(bean -> {
                    Class<?> targetClass = AopUtils.getTargetClass(bean);
                    logger.info("proxy class:{} ---->  target class:{} ", bean.getClass(), targetClass);
                    if (Objects.nonNull(targetClass.getAnnotation(clazz))) {
                        addRouter(targetClass, bean);
                    }
                }

        );
    }


    public void addRouter(final Class<?> clazz, Object instance) {
        try {
            final PathMapping classPathMapping = clazz.getAnnotation(PathMapping.class);
            String clazzUri = CommonConsts.EMPTY;
            if (classPathMapping != null) {
                final String uri = classPathMapping.uri();
                clazzUri = CommonConsts.SLASH + FileUtils.trim(uri);
            }
            final Method[] methods = clazz.getDeclaredMethods();
            for (Method invokeMethod : methods) {
                final Annotation[] annotations = invokeMethod.getAnnotations();
                for (Annotation annotation : annotations) {
                    final Class<? extends Annotation> annotationType = annotation.annotationType();
                    if (annotationType == PathMapping.class) {
                        final PathMapping methodPathMapping = (PathMapping) annotation;
                        final String methodUri = methodPathMapping.uri();
                        final String httpMethod = methodPathMapping.method().toString();
                        if (!controllerBeans.containsKey(clazz.getName())) {
                            controllerBeans.put(clazz.getName(), instance);
                        }
                        final RequestHandler requestHandler = new RequestHandler(controllerBeans.get(clazz.getName()), invokeMethod);
                        final String requestUri = clazzUri + CommonConsts.SLASH + FileUtils.trim(methodUri);
                        httpRouterMapper.put(new Request(requestUri, HttpMethod.valueOf(httpMethod)), requestHandler);
                    }
                }
            }
            String msg = "??????????????? ==> [{}], ???????????? ==> [{}], ???????????? ==> [{}]";
            httpRouterMapper.forEach((key, value) -> logger.info(msg, value.getObject(), key.getUri(), key.getMethod()));
        } catch (Exception e) {
            logger.error("load class error:{}", clazz, e);
        }
    }

    public RequestHandler getRoute(final Request request) {
        return httpRouterMapper.get(request);
    }
}
