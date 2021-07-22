package com.lhever.simpleim.router.basic.http;

import com.lhever.common.core.response.CommonResponse;
import com.lhever.simpleim.router.basic.http.annotation.RequestParam;
import com.lhever.simpleim.router.basic.util.RequestUtils;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class HttpMethodHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(HttpRouter.class);

    @NonNull
    private Object object;

    @NonNull
    private Method method;

    // private boolean injectionFullHttpRequest;

    private static void verifyParameterAnnotations(Map<String, List<String>> parameterMap, Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return;
        }
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestParam) {
                    RequestParam requestParam = (RequestParam) annotation;
                    if (requestParam.required()) {
                        String name = requestParam.name();
                        List<String> values = parameterMap.get(name);
                        if (CollectionUtils.isEmpty(values)) {
                            String defaultValue = requestParam.defaultValue();
                            if (StringUtils.isBlank(defaultValue)) {
                                throw new RuntimeException(String.format("Required parameter '%s' is not present.", name));
                            } else {
                                parameterMap.put(name, Collections.singletonList(defaultValue));
                            }
                        }
                    }
                }
            }
        }
    }

    private static void handleParameterTypes(Class<?> parameterType, List<String> values, List<Object> args) {
        if (CollectionUtils.isEmpty(values)) {
            args.add(null);
            return;
        }
        String value = values.iterator().next();
        if (parameterType.equals(String.class)) {
            args.add(value);
        } else if (parameterType.equals(Integer.class) || parameterType.equals(int.class)) {
            args.add(Integer.valueOf(value));
        } else if (parameterType.equals(Long.class) || parameterType.equals(long.class)) {
            args.add(Long.valueOf(value));
        } else if (parameterType.equals(Byte.class) || parameterType.equals(byte.class)) {
            args.add(Byte.valueOf(value));
        } else if (parameterType.equals(Float.class) || parameterType.equals(float.class)) {
            args.add(Float.valueOf(value));
        } else if (parameterType.equals(Double.class) || parameterType.equals(double.class)) {
            args.add(Double.valueOf(value));
        } else if (parameterType.equals(Boolean.class) || parameterType.equals(boolean.class)) {
            args.add(Boolean.valueOf(value));
        } else if (parameterType.equals(Short.class) || parameterType.equals(short.class)) {
            args.add(Short.valueOf(value));
        } else if (parameterType.isArray()) {
            Class<?> componentType = parameterType.getComponentType();
            if (componentType.equals(String.class)) {
                args.add(values.toArray(new String[0]));
            } else if (componentType.equals(Integer.class) || componentType.equals(int.class)) {
                Object array = Array.newInstance(componentType, values.size());
                for (int i = 0; i < values.size(); i++) {
                    Array.set(array, i, Integer.valueOf(values.get(i)));
                }
                args.add(array);
            } else if (componentType.equals(Long.class) || componentType.equals(long.class)) {
                Object array = Array.newInstance(componentType, values.size());
                for (int i = 0; i < values.size(); i++) {
                    Array.set(array, i, Long.valueOf(values.get(i)));
                }
                args.add(array);
            } else if (componentType.equals(Byte.class) || componentType.equals(byte.class)) {
                Object array = Array.newInstance(componentType, values.size());
                for (int i = 0; i < values.size(); i++) {
                    Array.set(array, i, Byte.valueOf(values.get(i)));
                }
                args.add(array);
            } else if (componentType.equals(Float.class) || componentType.equals(float.class)) {
                Object array = Array.newInstance(componentType, values.size());
                for (int i = 0; i < values.size(); i++) {
                    Array.set(array, i, Float.valueOf(values.get(i)));
                }
                args.add(array);
            } else if (componentType.equals(Double.class) || componentType.equals(double.class)) {
                Object array = Array.newInstance(componentType, values.size());
                for (int i = 0; i < values.size(); i++) {
                    Array.set(array, i, Double.valueOf(values.get(i)));
                }
                args.add(array);
            } else if (componentType.equals(Boolean.class) || componentType.equals(boolean.class)) {
                Object array = Array.newInstance(componentType, values.size());
                for (int i = 0; i < values.size(); i++) {
                    Array.set(array, i, Boolean.valueOf(values.get(i)));
                }
                args.add(array);
            } else if (componentType.equals(Short.class) || componentType.equals(short.class)) {
                Object array = Array.newInstance(componentType, values.size());
                for (int i = 0; i < values.size(); i++) {
                    Array.set(array, i, Short.valueOf(values.get(i)));
                }
                args.add(array);
            }
        }
    }

    public T call(FullHttpRequest request) {
        try {
            return (T) method.invoke(object, handleRequest(request));
        } catch (Exception e) {
            String message = e.getMessage();
            logger.error("Reasons for failure: {}", message);
            return (T) CommonResponse.clone("500", message);
        }
    }

    private Object[] handleRequest(FullHttpRequest request) {
        HttpMethod method = request.method();
        if (method == HttpMethod.GET) {
            return handleGetRequest(request);
        } else if (method == HttpMethod.POST) {
            return handlePostRequest(request);
        } else if (method == HttpMethod.PUT) {
            return handlePutRequest(request);
        } else if (method == HttpMethod.DELETE) {
            return handleDeleteRequest(request);
        }
        throw new RuntimeException(String.format("Unsupported '%s' request methods.", method));
    }

    private Object[] handleGetRequest(FullHttpRequest request) {
        Parameter[] parameters = method.getParameters();
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> args = new ArrayList<>(parameters.length);
        Map<String, List<String>> parameterMap = RequestUtils.getParameterMap(request);
        verifyParameterAnnotations(parameterMap, method);
        for (int i = 0; i < parameters.length; i++) {
            List<String> values = parameterMap.get(parameters[i].getName());
            handleParameterTypes(parameterTypes[i], values, args);
        }
        return args.toArray();
    }

    private Object[] handlePostRequest(FullHttpRequest request) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> args = Collections.singletonList(RequestUtils.postEntity(request, parameterTypes[0]));
        return args.toArray();
    }

    private Object[] handlePutRequest(FullHttpRequest request) {
        return handlePostRequest(request);
    }

    private Object[] handleDeleteRequest(FullHttpRequest request) {
        return handleGetRequest(request);
    }
}