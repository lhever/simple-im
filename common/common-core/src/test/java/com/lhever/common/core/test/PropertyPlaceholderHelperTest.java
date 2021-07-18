package com.lhever.common.core.test;

import com.lhever.common.core.support.parser.token.PropertyPlaceholderHelper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class PropertyPlaceholderHelperTest {

    @Test
    public void testPlaceHolder() {

        Map<String, String> data = new HashMap() {{
            put("name", "lihever");
            put("age", "28");
            put("email", "${name}@qq.com");
        }};


        PropertyPlaceholderHelper.PlaceholderResolver resolver = new PropertyPlaceholderHelper.PlaceholderResolver() {
            @Override
            public String resolvePlaceholder(String placeholderName) {
                return data.get(placeholderName);
            }
        };

        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");


        String text = "my name is ${name}, my email is ${email} ";
        System.out.println(helper.replacePlaceholders(text, resolver));

        System.out.println(helper.replacePlaceholders(text, data::get));


        PropertyPlaceholderHelper.PlaceholderResolver resolver1 = new PropertyPlaceholderHelper.PlaceholderResolver() {

            @Override
            public String resolvePlaceholder(String placeholderName) {
                return data.get(placeholderName);
            }
        };
        String text1 = "my name is ${name}, my email is ${email} ";
        System.out.println(helper.replacePlaceholders(text1, resolver1));
    }

    @Test
    public void testCircularRef() {
        Map<String, String> data = new HashMap() {{
            put("name", "lihever${email}");
            put("age", "28");
            put("email", "${name}@qq.com");
        }};

        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");

        IllegalArgumentException ex = null;
        String text = "my name is ${name}, my email is ${email} ";
        try {
            System.out.println(helper.replacePlaceholders(text, data::get));
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull("没有发生预期的循环引用", ex);
    }


    @Test
    public void testPlaceHolder1() {
        Map<String, String> data = new HashMap() {{
            put("server.port", "8001");
            put("spring.application.host", "10.12.66.50");
            put("statusPageUrl", "http://${spring.application.host}:${server.port}/das/status/reginfo");
            put("paramUrl", "${statusPageUrl}/?id=12345");

        }};

        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
        String text = "the port of das is ${server.port}, you can check status of das by visit ${paramUrl}";
        System.out.println(helper.replacePlaceholders(text, data::get));
    }


}
