package com.lhever.common.core.support.parser.markdown;

import com.lhever.common.core.utils.FileUtils;
import com.lhever.common.core.utils.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkDownUrlParser {
    private static String startToken = "![";
    private static String endToken = "]";
    private static String left_parentheses = "(";
    private static String right_parentheses = ")";
    private static String regx_md_url = "!\\[(.*?)\\]\\((.*?)\\)";

    /**
     * 该方法用于解析md文档中的url,markdown格式的文件中url格式类似：![](http://www.baidu.com),
     * 也就是说：实际的url是被左边的 "![](" 和右边的 ")" 包围的,另外 "![" 与 "]" 之间也可能包含有内容。
     * <p>
     * 该方法可以将markdown文件中的所有url解析出来，并以集合的形式返回
     *
     * @param text   被解析的文本
     * @param offset 解析文档的起始偏移量
     * @param urls   url集合
     * @return
     * @author lihong10 2019/3/1 16:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/1 16:44
     * @modify by reason:{原因}
     */
    private static Set<String> doParse(String text, int offset, Set<String> urls) {

        if (StringUtils.isBlank(text)) {
            return urls;
        }

        if (offset >= text.length()) {
            return urls;
        }


        int startIndex = text.indexOf(startToken, offset);
        if (isNotFind(startIndex)) {
            return urls;
        }

        int endIndex = text.indexOf(endToken, startIndex + startToken.length());
        if (isNotFind(endIndex)) {
            return urls;
        }


        int leftIndex = text.indexOf(left_parentheses, endIndex + endToken.length());
        if (isNotFind(leftIndex)) {
            return urls;
        }

        // ] 与 ( 之间若存在非空文本，则认为是一个不合法的url,跳过解析
        String substring = text.substring(endIndex + endToken.length(), leftIndex);
        if (StringUtils.isNotBlank(substring)) {
            //return doParse(text, leftIndex + left_parentheses.length(), urls);
            // 内容是： ![] ![](/imgs/xxx.png)， 上一条语句会导致解析遗漏
            return doParse(text, endIndex + endToken.length(), urls);
        }


        int rightIndex = text.indexOf(right_parentheses, leftIndex + left_parentheses.length());
        if (isNotFind(rightIndex)) {
            return urls;
        }

        String url = text.substring(leftIndex + 1, rightIndex);
        urls.add(url);
        return doParse(text, rightIndex + right_parentheses.length(), urls);
    }


    public static String md2String(String filePath) {
        File file = new File(filePath);
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 该方法用于解析md文档中的url
     *
     * @param text   被解析的md文本
     * @param offset 解析文档的起始偏移量/索引
     * @return
     * @author lihong10 2019/3/1 16:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/1 16:44
     * @modify by reason:{原因}
     */
    public static Set<String> parse(String text, int offset) {
        return doParse(text, offset, new LinkedHashSet<>());
    }

    /**
     * 该方法用于解析md文档中的url, 默认从索引0开始解析
     *
     * @param text 被解析的md文本
     * @return
     * @author lihong10 2019/3/1 16:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/1 16:44
     * @modify by reason:{原因}
     */
    public static Set<String> parse(String text) {
        return doParse(text, 0, new LinkedHashSet<>());
    }


    /**
     * 该方法使用正则表达式解析md文档中的url, markdown格式的文本中url格式类似：![](http://www.baidu.com),
     * 也就是说：实际的url是被左边的 "![](" 和右边的 ")" 包围的,另外 "![" 与 "]" 之间也可能包含有内容。
     * <p>
     * 该方法可以将markdown文件中的所有url解析出来，并以集合的形式返回
     *
     * @param text 被解析的文本
     * @param urls url集合
     * @return
     * @author lihong10 2019/3/1 16:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/1 16:44
     * @modify by reason:{原因}
     */
    private static Set<String> doParseByRegx(String text, Set<String> urls) {
        Pattern pattern = Pattern.compile(regx_md_url);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            //String name = matcher.group(1);
            String url = matcher.group(2);
            urls.add(url);
        }

        return urls;
    }

    /**
     * 该方法使用正则表达式解析md文档中的url
     * 该方法可以将markdown文件中的所有url解析出来，并以集合的形式返回
     *
     * @param text 被解析的文本
     * @return
     * @author lihong10 2019/3/1 16:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/1 16:44
     * @modify by reason:{原因}
     */
    public static Set<String> parseByRegx(String text) {
        return parseByRegx(text, 0);
    }

    /**
     * 该方法使用正则表达式解析md文档中的url
     * 该方法可以将markdown文件中的所有url解析出来，并以集合的形式返回
     *
     * @param text   被解析的文本
     * @param offset 解析文档的起始偏移量/索引
     * @return
     * @author lihong10 2019/3/1 16:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/1 16:44
     * @modify by reason:{原因}
     */
    public static Set<String> parseByRegx(String text, int offset) {
        LinkedHashSet<String> urls = new LinkedHashSet<>();
        if (StringUtils.isBlank(text)) {
            return urls;
        }

        if (offset >= text.length()) {
            return urls;
        }

        return doParseByRegx(text.substring(offset), urls);
    }


    private static boolean isNotFind(int index) {
        return !isFind(index);
    }

    private static boolean isFind(int index) {
        return index > -1;
    }


    @Test
    public void test1() {
        String content = "存储资源能力介绍![](/imgs/存储能力开放介绍.png)我是中托管![](/imgs/5d2f4d8a8e0a8dea9bfa04910fb3b071.png)" +
                "![img](/imgs/image3.png)提供视频、![] ![](/imgs/xxx.png)图片、文件、对象数据统一存储能力![](/imgs/image4.png)";

        Set<String> parse = parse(content, 0);
        System.out.println(parse);
        System.out.println(parse.size());
    }

    @Test
    public void test2() {
        /*String content = "存储资源能力介绍![](/imgs/存储能力开放介绍.png)我是中托管![](/imgs/5d2f4d8a8e0a8dea9bfa04910fb3b071.png)" +
                "![img](/imgs/image3.png)提供视频、![]xx  (/imgs/xxx.png)图片、文件、对象数据统一存储能力![](/imgs/image4.png)";

        Set<String> parse = parse(content);
        System.out.println(parse);
        System.out.println(parse.size());*/

        //
        String str = md2String("E:/temp/export/智能应用服务_徐玉龙5，施冲/智能应用服务_徐玉龙5，施冲.md");
        System.out.println(str);
    }


    @Test
    public void test3() {
        String content = FileUtils.readByPath("D:/doc.txt");
        System.out.println(content);
        Set<String> parse = parse(content, 0);
        System.out.println(parse);
        System.out.println(parse.size());
    }

    @Test
    public void test4() {
        String content = FileUtils.readByPath("D:/doc.txt");
        System.out.println(content);
        Set<String> parse = parse(content);
        System.out.println(parse);
        System.out.println(parse.size());
    }


    @Test
    public void parseByRegExp() {


        String content = "存储资源能力介绍![](/imgs/存储能力开放介绍.png)我是中托管![](/imgs/5d2f4d8a8e0a8dea9bfa04910fb3b071.png)" +
                "![img](/imgs/image3.png)提供视频、![]xx  ![](/imgs/xxx.png)图片、文件、对象数据统一存储能力![](/imgs/image4.png)";
        System.out.println(content);
        Set<String> parse = parseByRegx(content);
        System.out.println(parse);
        System.out.println(parse.size());
    }


    /**
     * 测试使用正则与非正则两种方式的性能
     *
     * @param
     * @return
     * @author lihong10 2019/3/1 19:08
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/1 19:08
     * @modify by reason:{原因}
     */
    @Test
    public void testPerformance() {

        String content = "存储资源能力介绍![](/imgs/存储能力开放介绍.png)我是中托管![](/imgs/5d2f4d8a8e0a8dea9bfa04910fb3b071.png)" +
                "![img](/imgs/image3.png)提供视频、![]xx  ![](/imgs/xxx.png)图片、文件、对象数据统一存储能力![](/imgs/image4.png)";

        //预热
        parse(content);
        parse(content);
        parseByRegx(content);
        parseByRegx(content);


        //打印两种方式得耗时
        int count = 10000000;

        long parseStart = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            parse(content);
        }
        System.out.println(System.currentTimeMillis() - parseStart);

        long regexpStart = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            parseByRegx(content);
        }
        System.out.println(System.currentTimeMillis() - regexpStart);
    }


}
