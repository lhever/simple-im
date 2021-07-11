package com.lhever.common.core.test;

import com.lhever.common.core.support.parser.token.AbstractTokenParser;
import com.lhever.common.core.support.parser.token.BeanTokenParser;
import com.lhever.common.core.utils.ParseUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParseUtilsTest {

    private static class ParserBean {
        private String name;
        private int age;
        private Integer status;

        public ParserBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }


    /**
     * 使用示例
     *
     * @return
     * @author lihong10 2019/1/11 19:17
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/11 19:17
     * @modify by reason:{原因}
     */
    @Test
    public void testParseUtils() {
        Map data = new HashMap<String, Object>() {{
            put("name", "雷锋");
            put("result", true);
            put("confidence", 99.9);
            put("", this);
        }};

        List<Object> li = new LinkedList<Object>() {{
            add("david");
            add(1);
            add(true);
            add(null);
            add(new Object());
            add(123.4);
            add(1000L);
        }};


        //{}被转义，不会被替换
        System.out.println(ParseUtils.parseArgs("我的名字是{},结果是{}，可信度是%{}, 不要替换我\\{}", "雷锋", true, 100, "NO"));
        System.out.println(ParseUtils.parseArgs("我的名字是{},结果是{}，可信度是%{}", "雷锋", true, 100));
        System.out.println(ParseUtils.parseMap("我的名字是{name},结果是{result}，可信度是%{confidence}, 执行类是{}", data));
        System.out.println(ParseUtils.parseList("{}-{}-{}-{}-{}-{}-{}-{}", li));
        System.out.println(ParseUtils.parseBean("{}-{}-{}-{}-{}-{}-{}-{}", null));

        AbstractTokenParser objParser = new BeanTokenParser(new ParserBean("李冰冰"), v -> "(" + v + ")", null);
        System.out.println(objParser.parse("{", "}", "{name}-{age}-{status}-{}-{}-{}-{}-{}"));

        AbstractTokenParser objParser1 = new BeanTokenParser(new ParserBean("李冰冰"), null, "未知值");
        System.out.println(objParser1.parse("{", "}", "{name}-{age}-{status}-{}-{}-{}-{}-{}"));

        AbstractTokenParser objParser2 = new BeanTokenParser(new ParserBean("李冰冰"), v -> v, null);
        System.out.println(objParser2.parse("{", "}", "{name}-{age}-{status}-{}-{}-{}-{}-{}"));

        AbstractTokenParser objParser3 = new BeanTokenParser(new ParserBean("李冰冰"), v -> v, "");
        System.out.println(objParser3.parse("{", "}", "{name}-{age}-{status}-{}-{}-{}-{}-{}"));

        System.out.println(ParseUtils.parseBean("{}-{}-{}-{}-{}-{}-{}-{}", null));
        System.out.println("******************************************");
        System.out.println(ParseUtils.parseList(null, li));

        System.out.println(ParseUtils.parseNoop("我的名字是{name},结果是{result}，可信度是%{confidence}, 执行类是{}"));
        System.out.println(ParseUtils.parseNoop("$[", "]", "我的名字是{$[name]},结果是$[result]，可信度是%{confidence}, 执行类是$[]"));
    }
}
