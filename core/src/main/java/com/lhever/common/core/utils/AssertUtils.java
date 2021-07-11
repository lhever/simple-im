package com.lhever.common.core.utils;

import com.lhever.common.core.exception.CommonErrorCode;
import com.lhever.common.core.exception.CommonException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/8/31 16:02
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/8/31 16:02
 * @modify by reason:{方法名}:{原因}
 */
public class AssertUtils {


    public static void state(boolean expression, String code, String message) {
        if (!expression) {
            throw new CommonException(code, message);
        }
    }


    public static void state(boolean expression, String code, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void isTrue(boolean expression, String code, String message) {
        if (!expression) {
            throw new CommonException(code, message);
        }
    }


    public static void isTrue(boolean expression, String code, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void isNull(Object object, String code, String message) {
        if (object != null) {
            throw new CommonException(code, message);
        }
    }


    public static void isNull(Object object, String code, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void notNull(Object object, String code, String message) {
        if (object == null) {
            throw new CommonException(code, message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new CommonException(CommonErrorCode.ILLEGAL_PARAM_EXCEPTION.getCode(), message);
        }
    }


    public static void notNull(Object object, String code, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }

    public static void notNull(Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new CommonException(CommonErrorCode.ILLEGAL_PARAM_EXCEPTION.getCode(), nullSafeGet(messageSupplier));
        }
    }


    public static void hasLength(String text, String code, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new CommonException(code, message);
        }
    }


    public static void hasLength(String text, String code, Supplier<String> messageSupplier) {
        if (StringUtils.isEmpty(text)) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void hasText(String text, String code, String message) {
        if (StringUtils.isBlank(text)) {
            throw new CommonException(code, message);
        }
    }


    public static void hasText(String text, String code, Supplier<String> messageSupplier) {
        if (StringUtils.isBlank(text)) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void doesNotContain(String textToSearch, String substring, String code, String message) {
        if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) &&
                textToSearch.contains(substring)) {
            throw new CommonException(code, message);
        }
    }


    public static void doesNotContain(String textToSearch, String substring, String code, Supplier<String> messageSupplier) {
        if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) &&
                textToSearch.contains(substring)) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void notEmpty(Object[] array, String code, String message) {
        if (array == null || array.length == 0) {
            throw new CommonException(code, message);
        }
    }


    public static void notEmpty(Object[] array, String code, Supplier<String> messageSupplier) {
        if ((array == null || array.length == 0)) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void noNullElements(Object[] array, String code, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new CommonException(code, message);
                }
            }
        }
    }


    public static void noNullElements(Object[] array, String code, Supplier<String> messageSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new CommonException(code, nullSafeGet(messageSupplier));
                }
            }
        }
    }


    public static void notEmpty(Collection<?> collection, String code, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new CommonException(code, message);
        }
    }


    public static void notEmpty(Collection<?> collection, String code, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void noNullElements(Collection<?> collection, String code, String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new CommonException(code, message);
                }
            }
        }
    }


    public static void noNullElements(Collection<?> collection, String code, Supplier<String> messageSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new CommonException(code, nullSafeGet(messageSupplier));
                }
            }
        }
    }

    public static void notEmpty(Map<?, ?> map, String code, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new CommonException(code, message);
        }
    }


    public static void notEmpty(Map<?, ?> map, String code, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(map)) {
            throw new CommonException(code, nullSafeGet(messageSupplier));
        }
    }


    public static void isInstanceOf(Class<?> type, Object obj, String code, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, code, message);
        }
    }


    public static void isInstanceOf(Class<?> type, Object obj, String code, Supplier<String> messageSupplier) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, code, nullSafeGet(messageSupplier));
        }
    }


    public static void isInstanceOf(Class<?> type, Object obj, String code) {
        isInstanceOf(type, obj, code, "");
    }


    public static void isAssignable(Class<?> superType, Class<?> subType, String code, String message) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, code, message);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String code, Supplier<String> messageSupplier) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, code, nullSafeGet(messageSupplier));
        }
    }


    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, CommonErrorCode.ILLEGAL_PARAM_EXCEPTION.getCode(), "");
    }


    private static void instanceCheckFailed(Class<?> type, Object obj, String code, String msg) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.isNotBlank(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object of class [" + className + "] must be an instance of " + type);
        }
        throw new CommonException(code, result);
    }

    private static void assignableCheckFailed(Class<?> superType, Class<?> subType, String code, String msg) {
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.isNotBlank(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            } else {
                result = messageWithTypeName(msg, subType);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + (subType + " is not assignable to " + superType);
        }
        throw new CommonException(code, result);
    }

    private static boolean endsWithSeparator(String msg) {
        return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
    }

    private static String messageWithTypeName(String msg, Object typeName) {
        return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
    }


    private static String nullSafeGet(Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }


}
