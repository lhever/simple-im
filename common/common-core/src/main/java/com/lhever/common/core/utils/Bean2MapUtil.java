package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * 该类用于将对象转成map并返回
 *
 * @author lihong10 2017-10-31 上午11:04:10
 * @version v1.0
 */
public class Bean2MapUtil {
    private static final Logger log = LoggerFactory.getLogger(Bean2MapUtil.class);

    /**
     * 将bean的所有属性存为map， 包括null
     *
     * @param bean bean
     * @return object map
     * @author lihong10 2015-5-7 下午3:18:56
     */
    @SuppressWarnings("unused")
    public static Map<String, Object> toMap(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        List<String> includedAndAlias = new ArrayList<String>();
        int i = 0;
        for (Field f : fields) {
            //内部类默认拥有此属性， 不应该写入map
            if ("this$0".equals(f.getName())) {
                continue;
            }
            includedAndAlias.add(f.getName());
        }
        return toMap(bean, includedAndAlias.toArray(new String[includedAndAlias.size()]));
    }

    /**
     * 将Java bean转换为map&lt;String,Object&gt;对象, 可指定包含的属性及属性别名<br>
     * 如 Map&lt;String, Object&gt; objectMap = toMap(object, new String[]{"id:userId","name","age"})<br>
     * 这个调用将object中的id, name, age 字段名和字段值作为key-value对映射到map中, 并给id指定了别名userId<br>
     *
     * @param bean             java bean
     * @param includedAndAlias 要包含的属性[:别名]
     * @return object map
     * @author lihong10 2015-4-20 下午7:45:38
     */
    public static Map<String, Object> toMap(Object bean, String[] includedAndAlias) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap<String, Object>();
        for (String nameAndAlias : includedAndAlias) {
            String[] names = nameAndAlias.split(":");
            String fieldName = names[0];
            String mapName = fieldName;
            if (names.length > 1) {
                mapName = names[1];
            }

            boolean hasField = false;
            Field target = null;
            for (Field f : fields) {
                if (fieldName.equals(f.getName())) {
                    hasField = true;
                    target = f;
                    break;
                }
            }
            if (!hasField) {
                throw new RuntimeException(String.format("there is no field named '%s' declared in %s", fieldName, bean.getClass().getName()));
            }
            target.setAccessible(true);
            try {
                map.put(mapName, target.get(bean));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.error("", e);
            }
        }
        return map;
    }

    /**
     * 将实体list转换成map list
     *
     * @param list
     * @param includedAndAlias
     * @return List<Map   <   String   ,       Object>>
     * @author lihong10 2015-6-30 下午4:38:28
     */
    @SuppressWarnings("rawtypes")
    public static List<Map<String, Object>> toMap(List list, String[] includedAndAlias) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Object obj : list) {
            data.add(toMap(obj, includedAndAlias));
        }
        return data;
    }

    /**
     * 将实体list转换成map list
     *
     * @param list
     * @return List<Map   <   String   ,       Object>>
     * @author lihong10 2015-7-22 上午11:12:29
     */
    @SuppressWarnings("rawtypes")
    public static List<Map<String, Object>> toMap(List list) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Object obj : list) {
            data.add(toMap(obj));
        }
        return data;
    }

    /**
     * 将Java bean转换为map&lt;String,Object&gt;对象, 可指定包含的属性及属性别名<br>
     * 如 Map&lt;String, Object&gt; objectMap = toMap(object, new String[]{"id:userId","name","age"})<br>
     * 这个调用将object中的id, name, age 字段名和字段值作为key-value对映射到map中, 并给id指定了别名userId<br>
     *
     * @param bean          java bean
     * @param differenceSet 要包含的属性[:别名]
     * @return object map
     * @author lihong10 2015-4-20 下午7:45:38
     */
    public static Map<String, Object> toMapByDifferenceSet(Object bean, String[] differenceSet) {
        Field[] fields = bean.getClass().getDeclaredFields();
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> differenceSetList = new ArrayList<String>(Arrays.asList(differenceSet));
        for (Field field : fields) {
            boolean isInDifferenceSet = false;
            for (Iterator<String> iterator = differenceSetList.iterator(); iterator.hasNext(); ) {
                String difference = (String) iterator.next();
                if (field.getName().equals(difference)) {
                    isInDifferenceSet = true;
                    iterator.remove();
                    break;
                }
            }
            if (!isInDifferenceSet) {
                field.setAccessible(true);
                try {
                    map.put(field.getName(), field.get(bean));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error("", e);
                }
            }
        }
        if (differenceSetList.isEmpty() == false) {
            throw new RuntimeException(String.format("there is no field named '%s' declared in %s where runing method toMapByDifferenceSet(Object bean, String[] differenceSet)",
                    differenceSetList.toString(),
                    bean.getClass().getName()));
        }

        return map;
    }

    /**
     * 将实体list转换成map list
     *
     * @param list
     * @param differenceSet
     * @return List<Map   <   String   ,       Object>>
     * @author lihong10 2015-6-30 下午4:38:28
     */
    @SuppressWarnings("rawtypes")
    public static List<Map<String, Object>> toMapByDifferenceSet(List list, String[] differenceSet) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Object obj : list) {
            data.add(toMapByDifferenceSet(obj, differenceSet));
        }
        return data;
    }


    /**
     * 获取对象的某个属性值
     *
     * @param propertyName
     * @param obj
     * @return 属性值
     * @author lihong10 2017-10-31 下午12:12:09
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object getProperty(final Object obj, String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            return null;
        }
        Class cls = obj.getClass();
        try {
            final Field field = cls.getDeclaredField(propertyName);
            return AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    Object result = null;
                    try {
                        result = field.get(obj);
                    } catch (IllegalAccessException e) {
                        //error wont' happen
                        log.error("", e);
                    }
                    field.setAccessible(accessible);
                    return result;
                }
            });
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     *
     * @param clazz        类型
     * @param propertyName 属性名
     * @return 返回对应的Field
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     * @author lihong10 2017-10-31 下午12:12:09
     */
    @SuppressWarnings("rawtypes")
    public static Field getDeclaredField(Class clazz, String propertyName)
            throws NoSuchFieldException {

        for (Class superClass = clazz; superClass != Object.class;
             superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException ex) {
                // Field不在当前类定义,继续向上转型
                log.error("", ex);
            }
        }

        throw new NoSuchFieldException("No such field: " + clazz.getName()
                + '.' + propertyName);
    }

    /**
     * 强制设置对象变量值,忽略private,protected修饰符的限制.
     *
     * @param object       对象实例
     * @param propertyName 属性名
     * @param newValue     赋予的属性值
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     * @author lihong10 2017-10-31 下午12:12:09
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void forceSetProperty(final Object object,
                                        final String propertyName, final Object newValue)
            throws NoSuchFieldException {

        final Field field = getDeclaredField(object.getClass(), propertyName);

        AccessController.doPrivileged(new PrivilegedAction() {
            /** * run. */
            public Object run() {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);

                try {
                    field.set(object, newValue);
                } catch (IllegalAccessException e) {
                    //"Error won't happen
                    log.error("", e);
                }
                field.setAccessible(accessible);
                return null;
            }
        });
    }

}
