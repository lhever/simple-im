package com.lhever.simpleim.router.basic.util;

import com.lhever.simpleim.router.basic.cfg.RouterConfig;
import com.lhever.simpleim.router.basic.http.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/23 22:02
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/23 22:02
 * @modify by reason:{方法名}:{原因}
 */
public class ParamNameResolver {

    private static final String GENERIC_NAME_PREFIX = "param";

    /**
     * <p>
     * The key is the index and the value is the name of the parameter.<br />
     * The name is obtained from {@link RequestParam} if specified. When {@link RequestParam} is not specified,
     * the parameter index is used.
     * </p>
     * <ul>
     * <li>aMethod(@RequestParam("M") int a, @RequestParam("N") int b) -&gt; {{0, "M"}, {1, "N"}}</li>
     * <li>aMethod(int a, int b) -&gt; {{0, "0"}, {1, "1"}}</li>
     * </ul>
     */

    public static SortedMap<Integer, String> ParamNameResolver(Method method) {
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        final SortedMap<Integer, String> map = new TreeMap<>();
        int paramCount = paramAnnotations.length;
        // get names from @Param annotations
        for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
            String name = null;
            for (Annotation annotation : paramAnnotations[paramIndex]) {
                if (annotation instanceof RequestParam) {
                    name = ((RequestParam) annotation).name();
                    break;
                }
            }
            if (name == null) {
                // @Param was not specified.
                if (RouterConfig.USE_ACTUAL_PARAM_NAME) {
                    name = getActualParamName(method, paramIndex);
                }
                if (name == null) {
                    // use the parameter index as the name ("0", "1", ...)
                    // gcode issue #71
                    name = String.valueOf(map.size());
                }
            }
            map.put(paramIndex, name);
        }
        return Collections.unmodifiableSortedMap(map);
    }

    private static String getActualParamName(Method method, int paramIndex) {
        return getParamNames(method).get(paramIndex);
    }



    /**
     * Returns parameter names referenced by SQL providers.
     */
    public String[] getNames(SortedMap<Integer, String> names) {
        return names.values().toArray(new String[0]);
    }

    /**
     * <p>
     * A single non-special parameter is returned without a name.
     * Multiple parameters are named using the naming rule.
     * In addition to the default names, this method also adds the generic names (param1, param2,
     * ...).
     * </p>
     */
    public Object getNamedParams(Object[] args, SortedMap<Integer, String> names) {
        final int paramCount = names.size();
        if (args == null || paramCount == 0) {
            return null;
        } else if (paramCount == 1) {
            return args[names.firstKey()];
        } else {
            final Map<String, Object> param = new HashMap();
            int i = 0;
            for (Map.Entry<Integer, String> entry : names.entrySet()) {
                param.put(entry.getValue(), args[entry.getKey()]);
                // add generic param names (param1, param2, ...)
                final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
                // ensure not to overwrite parameter named with @Param
                if (!names.containsValue(genericParamName)) {
                    param.put(genericParamName, args[entry.getKey()]);
                }
                i++;
            }
            return param;
        }
    }




    public static List<String> getParamNames(Method method) {
        return getParameterNames(method);
    }

    public static List<String> getParamNames(Constructor<?> constructor) {
        return getParameterNames(constructor);
    }

    private static List<String> getParameterNames(Executable executable) {
        return Arrays.stream(executable.getParameters()).map(Parameter::getName).collect(Collectors.toList());
    }



}
