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

public class HttpRouter/* extends ClassLoader*/ {

    private static final Logger logger = LoggerFactory.getLogger(HttpRouter.class);

    private static final int BUFFER_SIZE = 1024 * 8;
    private Map<HttpHandlerPath, HttpMethodHandler> httpRouterMapper = Maps.newConcurrentMap();
    private String classpath = this.getClass().getResource(CommonConsts.EMPTY).getPath();
    private Map<String, Object> controllerBeans = Maps.newConcurrentMap();

   /* @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final String path = classpath + name.replaceAll("\\.", CommonConsts.SLASH);
        byte[] bytes;
        try (InputStream ins = new FileInputStream(path)) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                final byte[] buffer = new byte[BUFFER_SIZE];
                int temp;
                while ((temp = ins.read(buffer)) != -1) {
                    out.write(buffer, 0, temp);
                }
                bytes = out.toByteArray();
            }
        } catch (Exception e) {
            throw new ClassNotFoundException(name);
        }
        return super.defineClass(name, bytes, 0, bytes.length);
    }*/


    public void loadControllerClass(ApplicationContext ctx) {
        final Class<? extends Annotation> clazz = RestController.class;
        ctx.getBeansWithAnnotation(clazz)
                .values().stream().forEach(bean -> {
                    Class<?> targetClass = AopUtils.getTargetClass(bean);
                    logger.info("scan controller class, aop class:{} ---->  target class:{} ");
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
                        final HttpMethodHandler httpMethodHandler = new HttpMethodHandler(controllerBeans.get(clazz.getName()), invokeMethod);
                        final String requestUri = clazzUri + CommonConsts.SLASH + FileUtils.trim(methodUri);
                        httpRouterMapper.put(new HttpHandlerPath(requestUri, HttpMethod.valueOf(httpMethod)), httpMethodHandler);
                    }
                }
            }
            String msg = "加载控制层 ==> [{}], 请求路径 ==> [{}], 请求方法 ==> [{}]";
            httpRouterMapper.forEach((key, value) -> logger.info(msg, value.getObject(), key.getUri(), key.getMethod()));
        } catch (Exception e) {
            logger.error("load class error:{}", clazz, e);
        }
    }

    public HttpMethodHandler getRoute(final HttpHandlerPath httpHandlerPath) {
        return httpRouterMapper.get(httpHandlerPath);
    }
}
