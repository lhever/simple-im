package com.lhever.common.core.support.parser.token;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

public class PropertyPlaceholderHelper {

    private static final Log logger = LogFactory.getLog(PropertyPlaceholderHelper.class);

    private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<>(4);

    static {
        wellKnownSimplePrefixes.put("}", "{");
        wellKnownSimplePrefixes.put("]", "[");
        wellKnownSimplePrefixes.put(")", "(");
    }

    private final String placeholderPrefix;

    private final String placeholderSuffix;

    private final String simplePrefix;

    /*
     * 默认值分隔符，可以为空
     */
    @Nullable
    private final String valueSeparator;

    private final boolean ignoreUnresolvablePlaceholders;

    /**
     * 根据指定的前缀和后缀创建占位符解析类
     *
     * @param placeholderPrefix 占位符的前缀字符串，用于标记占位符的起始位置
     * @param placeholderSuffix 占位符的后缀字符串，用于标记占位符的结束位置
     * @return
     * @author lihong10 2019/1/25 14:19
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/25 14:19
     * @modify by reason:{原因}
     */
    public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {
        this(placeholderPrefix, placeholderSuffix, null, true);
    }

    /**
     * PropertyPlaceholderHelper构造方法
     *
     * @param placeholderPrefix              占位符的前缀字符串，用于标记占位符的起始位置
     * @param placeholderSuffix              占位符的后缀字符串，用于标记占位符的结束位置
     * @param valueSeparator                 分隔符， 分隔符之前部分是占位符关联的值，分隔符之后是关联的默认值
     * @param ignoreUnresolvablePlaceholders
     * @return true, 遇到无法解析的占位符，跳过。 false, 遇到无法解析的占位符，抛出异常
     * @author lihong10 2019/1/25 14:21
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/25 14:21
     * @modify by reason:{原因}
     */
    public PropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix,
                                     @Nullable String valueSeparator, boolean ignoreUnresolvablePlaceholders) {

        Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
        Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
        String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
        if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
            this.simplePrefix = simplePrefixForSuffix;
        } else {
            this.simplePrefix = this.placeholderPrefix;
        }
        this.valueSeparator = valueSeparator;
        this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
    }


    /**
     * 根据占位符${xxx}名称xxx, 通过调用properties.get(xxx)获取值，并替换到占位位置
     *
     * @param value      占位符名称
     * @param properties 占位符对应的值，通过调用properties.get(value)获取
     * @return
     * @author lihong10 2019/1/25 14:24
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/25 14:24
     * @modify by reason:{原因}
     */
    public String replacePlaceholders(String value, final Properties properties) {
        Assert.notNull(properties, "'properties' must not be null");
        return replacePlaceholders(value, properties::getProperty);
    }

    /**
     * 根据占位符${xxx}名称xxx, 通过调用placeholderResolver.resolvePlaceholder(xxx)获取值，并替换到占位位置
     *
     * @param value               占位符名称
     * @param placeholderResolver 能够根据占位符返回关联值的解析类
     * @return
     * @author lihong10 2019/1/25 14:24
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/25 14:24
     * @modify by reason:{原因}
     */
    public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver) {
        Assert.notNull(value, "'value' must not be null");
        return parseStringValue(value, placeholderResolver, new HashSet<>());
    }

    /**
     * 替换文本中占位符的核心方法。该方法是递归方法。
     *
     * @param value               文本字符串
     * @param placeholderResolver 能够根据占位符返回关联值的占位符解析实现类
     * @param visitedPlaceholders 用于记录是否发生占位符循环引用。
     * @return
     * @author lihong10 2019/1/25 14:31
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/25 14:31
     * @modify by reason:{原因}
     */
    protected String parseStringValue(
            String value, PlaceholderResolver placeholderResolver, Set<String> visitedPlaceholders) {

        StringBuilder result = new StringBuilder(value);

        int startIndex = value.indexOf(this.placeholderPrefix);
        while (startIndex != -1) {
            int endIndex = findPlaceholderEndIndex(result, startIndex);
            if (endIndex != -1) {
                String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
                String originalPlaceholder = placeholder;
                if (!visitedPlaceholders.add(originalPlaceholder)) {
                    throw new IllegalArgumentException(
                            "Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
                }
                // Recursive invocation, parsing placeholders contained in the placeholder key.
                placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);
                // Now obtain the value for the fully resolved key...
                String propVal = placeholderResolver.resolvePlaceholder(placeholder);
                if (propVal == null && this.valueSeparator != null) {
                    int separatorIndex = placeholder.indexOf(this.valueSeparator);
                    if (separatorIndex != -1) {
                        String actualPlaceholder = placeholder.substring(0, separatorIndex);
                        String defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
                        propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
                        if (propVal == null) {
                            propVal = defaultValue;
                        }
                    }
                }
                if (propVal != null) {
                    // Recursive invocation, parsing placeholders contained in the
                    // previously resolved placeholder value.
                    propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
                    result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Resolved placeholder '" + placeholder + "'");
                    }
                    startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
                } else if (this.ignoreUnresolvablePlaceholders) {
                    // Proceed with unprocessed value.
                    startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
                } else {
                    throw new IllegalArgumentException("Could not resolve placeholder '" +
                            placeholder + "'" + " in value \"" + value + "\"");
                }
                visitedPlaceholders.remove(originalPlaceholder);
            } else {
                startIndex = -1;
            }
        }

        return result.toString();
    }

    /**
     * 判断buf从索引位置startIndex往后，是否包含 placeholderSuffix 子字符串。
     * 如果包含，返回目标子串出现的起始索引
     * 如果不包含，返回-1
     *
     * @param buf        被查找的字符串对象
     * @param startIndex 起始索引
     * @return
     * @author lihong10 2019/1/25 14:33
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/25 14:33
     * @modify by reason:{原因}
     */
    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + this.placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + this.placeholderSuffix.length();
                } else {
                    return index;
                }
            } else if (StringUtils.substringMatch(buf, index, this.simplePrefix)) {
                withinNestedPlaceholder++;
                index = index + this.simplePrefix.length();
            } else {
                index++;
            }
        }
        return -1;
    }

    /**
     * 能够根据占位符返回关联值的策略类接口，该接口是一个函数式接口，
     *
     * @author lihong10 2019/1/25 14:15
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/25 14:15
     * @modify by reason:{原因}
     */
    @FunctionalInterface
    public interface PlaceholderResolver {

        /**
         * resolvePlaceholder方法接收占位符字符串，返回与占位符号关联的值
         *
         * @param placeholderName 占位符名称
         * @return 与占位符关联的值，如果没有关联值，应该返回null
         * @author lihong10 2019/1/25 14:17
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2019/1/25 14:17
         * @modify by reason:{原因}
         */
        @Nullable
        String resolvePlaceholder(String placeholderName);
    }


}
