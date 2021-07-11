package com.lhever.common.core.utils;

import com.lhever.common.core.support.parser.token.*;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 将字符串text中由openToken(目前默认是{)和closeToken（目前默认是}）组成的占位符依次替换为特定的值。
 * 该方法核心代码取自mybatis框架GenericTokenParser.java,该类是mybatis默认的占位符解析器，性能极高。
 *
 * @author lihong10 2019/1/11 19:18
 * @return
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/1/11 19:18
 * @modify by reason:{原因}
 */
public class ParseUtils {

    private ParseUtils() {
    }

    /**
     * 使用map中的值替换占位符{xxx}, 前提是：xxx与map中的key相等
     *
     * @param text
     * @param map
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static <V> String parseMap(String text, Map<String, V> map) {
        AbstractTokenParser parser = new MapTokenParser<V>(map);
        return doParse(parser, "{", "}", text);
    }


    /**
     * 使用map中的值替换占位符 open + xxx + close, 前提是：xxx与map中的key相等
     *
     * @param openToken
     * @param closeToken
     * @param text
     * @param map
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static <V> String parseMap(String openToken, String closeToken, String text, Map<String, V> map) {
        AbstractTokenParser parser = new MapTokenParser<V>(map);
        return doParse(parser, openToken, closeToken, text);
    }


    /**
     * 使用可变参数值替换占位符{xxx}, 第一个可变参数替换到第一个占位符的位置，第二个可变参数替换到第二个占位符的位置，以此类推
     *
     * @param text
     * @param args
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static String parseArgs(String text, Object... args) {
        AbstractTokenParser parser = new ArrayTokenParser(args);
        return doParse(parser, "{", "}", text);
    }

    /**
     * 使用可变参数值替换占位符 openToken + xxx + closeToken, 第一个可变参数替换到第一个占位符的位置，第二个可变参数替换到第二个占位符的位置，以此类推
     *
     * @param openToken
     * @param closeToken
     * @param text       text是占位符的名称，由closeToken和closeToken包围形成
     * @param args
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    // parseArgsByToken 该方法之所以不命名为 parseArgs, 是因为编译器无法区分另外一个重载方法：
    public static String parseArgsByToken(String openToken, String closeToken, String text, Object... args) {
        AbstractTokenParser parser = new ArrayTokenParser(args);
        return doParse(parser, openToken, closeToken, text);
    }

    /**
     * 使用Properties中的值替换占位符{xxx} , 前提是：xxx与Properties.class中的key相等
     *
     * @param text
     * @param prop
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static String parseProp(String text, Properties prop) {
        AbstractTokenParser parser = new PropertiesTokenParser(prop);
        return doParse(parser, "{", "}", text);
    }

    /**
     * 使用Properties中的值替换占位符 openToken + xxx + closeToken, 前提是：xxx与Properties.class中的key相等
     *
     * @param openToken
     * @param closeToken
     * @param text
     * @param prop
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static String parseProp(String openToken, String closeToken, String text, Properties prop) {
        AbstractTokenParser parser = new PropertiesTokenParser(prop);
        return doParse(parser, openToken, closeToken, text);
    }

    /**
     * 使用链表元素替换占位符{xxx}, 第一个元素替换到第一个占位符的位置，第二个元素替换到第二个占位符的位置，以此类推
     *
     * @param text
     * @param list
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static <E> String parseList(String text, List<E> list) {
        AbstractTokenParser parser = new ListTokenParser<E>(list);
        return doParse(parser, "{", "}", text);
    }

    /**
     * 使用链表元素替换占位符 openToken + xxx + closeToken, 第一个元素替换到第一个占位符的位置，第二个元素替换到第二个占位符的位置，以此类推
     *
     * @param openToken
     * @param closeToken
     * @param text
     * @param list
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static <E> String parseList(String openToken, String closeToken, String text, List<E> list) {
        AbstractTokenParser parser = new ListTokenParser<E>(list);
        return doParse(parser, openToken, closeToken, text);
    }

    /**
     * 使用bean中的属性值替换占位符 {xxx}, 前提是：bean的属性名与占位符xxx相等
     *
     * @param text
     * @param bean
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static String parseBean(String text, Object bean) {
        AbstractTokenParser parser = new BeanTokenParser(bean);
        return doParse(parser, "{", "}", text);
    }

    /**
     * 使用bean中的属性值替换占位符 open + xxx + close, 前提是：bean的属性名与占位符xxx相等
     *
     * @param openToken
     * @param closeToken
     * @param text
     * @param bean
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static String parseBean(String openToken, String closeToken, String text, Object bean) {
        AbstractTokenParser parser = new BeanTokenParser(bean);
        return doParse(parser, openToken, closeToken, text);
    }

    /**
     * 保留占位符，但是移除占位符左侧的'{'和右侧的 '}'
     *
     * @param text
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static String parseNoop(String text) {
        AbstractTokenParser parser = new NoopTokenParser();
        return doParse(parser, "{", "}", text);
    }

    /**
     * 保留占位符，但是移除占位符左侧的 openToken和右侧的 closeToken
     *
     * @param openToken
     * @param closeToken
     * @param text
     * @return
     * @author lihong10 2019/1/18 10:45
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/18 10:45
     * @modify by reason:{原因}
     */
    public static String parseNoop(String openToken, String closeToken, String text) {
        AbstractTokenParser parser = new NoopTokenParser();
        return doParse(parser, openToken, closeToken, text);
    }

    public static String doParse(AbstractTokenParser parser, String openToken, String closeToken, String text) {
        return parser.parse(openToken, closeToken, text);
    }


}

