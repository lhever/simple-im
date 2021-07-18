package com.lhever.common.core.support.dfa;

import com.lhever.common.core.utils.StringUtils;
import org.apache.tika.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 查询敏感词的:  "确定有限自动状态机(DFA)"
 * </p>
 *
 * @author lihong10 2020/9/4 17:00
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/9/4 17:00
 * @modify by reason:{方法名}:{原因}
 */
public class KeyWordDFA {

    private static Logger logger = LoggerFactory.getLogger(KeyWordDFA.class);

    private static String END_KEY = "isEnd";
    private static Boolean YES = Boolean.TRUE;
    private static Boolean NO = Boolean.FALSE;

    public enum MatchType {
        //按照最短长度匹配
        MIN_LEN_MATCH,
        //按照最大长度匹配
        MAX_LEN_MATCH;
    }

    public enum ReplaceType {
        //优先替换最短的关键字
        MIN_FIRST,
        //优先替换最长的关键字
        MAX_FIRST,
        //替换时候不遵循字符串的长短顺序
        RANDOM;
    }


    private final Map keyWordMap;

    public KeyWordDFA() {
        HashMap map = new HashMap(0, 1.0f);
        map.put(END_KEY, NO);
        this.keyWordMap = map;
    }

    public void addKeyword(String... keyWords) {
        if (keyWords == null || keyWords.length == 0) {
            return;
        }
        List<String> keywordList = Arrays.asList(keyWords);
        //按照降序排序再添加到DFA状态机, 好处是不会导致较长的关键词覆盖较短的关键词的终止标志位
        Collections.sort(keywordList, (a, b) -> -1 * a.compareTo(b));//降序排序
        for (String keyword : keyWords) {
            addKeyword(keyword);
        }
    }

    /**
     * 封装敏感词库
     *
     * @param keyWordSet
     */
    public void addKeyword(Collection<String> keyWordSet) {
        if (keyWordSet == null || keyWordSet.size() == 0) {
            return;
        }
        List<String> keywordList = new ArrayList<>(keyWordSet);
        //按照降序排序再添加到DFA状态机, 好处是不会导致较长的关键词覆盖较短的关键词的终止标志位
        Collections.sort(keywordList, (a, b) -> -1 * a.compareTo(b));//降序排序
        for (String keyword : keywordList) {
            addKeyword(keyword);
        }
    }


    public void addKeyword(String keyword) {
        if (keyword == null || (keyword = keyword.trim()).length() == 0) {
            return;
        }

        // 使用一个迭代器来循环敏感词集合
        // 等于敏感词库，HashMap对象在内存中占用的是同一个地址，所以此nowMap对象的变化，sensitiveWordMap对象也会跟着改变
        Map currentMap = this.keyWordMap; // 初始化HashMap对象并控制容器的大小
        for (int i = 0; i < keyword.length(); i++) {
            // 截取敏感词当中的字，在敏感词库中字为HashMap对象的Key键值
            char keyChar = keyword.charAt(i);

            // 判断这个字是否存在于敏感词库中
            Object wordMap = currentMap.get(keyChar);
            if (wordMap != null) {
                currentMap = (Map) wordMap;
            } else {
                Map newMap = new HashMap(0, 1.0f);
                newMap.put(END_KEY, NO);
                currentMap.put(keyChar, newMap);
                currentMap = newMap;
            }

            //如果该字是当前敏感词的最后一个字，则标识为结尾字
            if (i == keyword.length() - 1) {
                currentMap.put(END_KEY, YES);
            }
            //logger.info("封装敏感词库过程:\n\r" + JSON.toJSONString(keyWordMap, true));
        }
//        logger.info("最终的敏感词库数据库是:\n\r" + JSON.toJSONString(keyWordMap, true));
    }


    /**
     * 搜索敏感词
     *
     * @param txt
     * @param matchType
     * @return
     * @author lihong10 2020/9/4 21:38
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/9/4 21:38
     * @modify by reason:{原因}
     */
    public Set<String> searchKeyword(String txt, MatchType matchType) {
        Set<String> keywordSet = new HashSet<>();
        for (int i = 0; i < txt.length(); i++) {
            int length = getMatchLenFrom(txt, i, matchType);
            if (length > 0) {
                // 将检测出的敏感词保存到集合中
                keywordSet.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return keywordSet;
    }


    /**
     * 从指定索引开始, 计算敏感词匹配长度，
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return
     * @author lihong10 2020/9/4 21:39
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/9/4 21:39
     * @modify by reason:{原因}
     */
    public int getMatchLenFrom(String txt, int beginIndex, MatchType matchType) {
        //是否连续匹配并最终达到关键词的末尾(isEnd=true代表末尾)
        boolean reachEnd = false;
        // 记录敏感词连续匹配长度
        int matchLen = 0;
        Map currentMap = keyWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            char word = txt.charAt(i);
            // 判断该字是否存在于敏感词库中
            currentMap = (Map) currentMap.get(word);
            if (currentMap != null) {
                matchLen++;
                // 判断是否是敏感词的结尾字，如果是结尾字则判断是否继续检测
                if (currentMap.get(END_KEY) == YES) {
                    reachEnd = true;
                    // 判断过滤类型，如果是小过滤则跳出循环，否则继续循环
                    if (MatchType.MIN_LEN_MATCH == matchType) {
                        break;
                    }
//                    logger.info("中间状态的匹配单词:" + txt.substring(beginIndex, beginIndex + matchLen));
                }
            } else {
                break;
            }
        }
        if (!reachEnd) {
            matchLen = 0;
        }
        return matchLen;
    }


    /**
     * searchKeyword2的第二种实现，代码晦涩,建议使用searchKeyword，不要使用该方法
     * @author lihong10 2020/9/4 23:34
     * @param text
     * @param matchType
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/9/4 23:34
     * @modify by reason:{原因}
     */
    public List<String> searchKeyword2(String text, MatchType matchType) {
        List<String> words = new ArrayList<>();

        if (text == null || text.length() == 0) {
            return words;
        }

        int rollback = 0; // 回滚数
        int position = 0; // 当前比较的位置

        boolean preIsEnd = false;

        Map temp = keyWordMap;
        StringBuilder builder = new StringBuilder();
        while (position < text.length()) {
            char keyChar = text.charAt(position);
            temp = (Map) temp.get(keyChar);
            // 当前位置的匹配结束
            if (temp == null) {
                if (preIsEnd == true && matchType != MatchType.MIN_LEN_MATCH) {
                    words.add(builder.toString());
                    preIsEnd = false;
                }
                builder.setLength(0);
                position = position - rollback; // 回退 并测试下一个字节
                rollback = 0;
                temp = keyWordMap; // 状态机复位
            } else if (temp.get(END_KEY) == YES) { // 是结束点 记录关键词
                builder.append(keyChar);
                if (matchType == MatchType.MIN_LEN_MATCH || position == (text.length() - 1)) { //不是最短匹配，但是达到了单词末尾,也认为匹配结束
                    words.add(builder.toString());
                    builder.setLength(0);
                    temp = keyWordMap; //如果是最短匹配，找到单词后重置状态机器
                    rollback = 0;
                } else {
                    preIsEnd = true;
                    rollback++; // 遇到结束点 rollback置为1
                }
            } else {
                builder.append(keyChar);
                rollback++; // 非结束点 回退数加1
            }
            position++;
        }

        return words;
    }


    /**
     * 替换敏感词
     *
     * @param txt
     * @param matchType
     * @param replaceChar
     * @return
     * @author lihong10 2020/9/4 21:55
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/9/4 21:55
     * @modify by reason:{原因}
     */
    public String replaceKeyword(String txt, MatchType matchType, String replaceChar, ReplaceType replaceType) {
        String resultTxt = txt;
        Collection<String> keywords = searchKeyword(txt, matchType);
        replaceType = replaceType == null ? ReplaceType.RANDOM : replaceType;
        switch (replaceType) {
            case RANDOM:
                break; //不排序，跳过
            case MIN_FIRST:
                List<String> ascList = new ArrayList<>(keywords);
                Collections.sort(ascList, (a, b) -> a.compareTo(b));//升序排序是为了先替换最短的关键词
                keywords = ascList;
                break;
            case MAX_FIRST:
                List<String> descList = new ArrayList<>(keywords);
                Collections.sort(descList, (a, b) -> -1 * a.compareTo(b));//降序排序是为了先替换最长的关键词
                keywords = descList;
                break;
        }
        for (String keyword : keywords) {
            String replaceString = repeatN(replaceChar, keyword.length());
            resultTxt = resultTxt.replaceAll(keyword, replaceString);
        }
        return resultTxt;
    }

    private String repeatN(String replaceChar, int N) {
        String result = replaceChar;
        for (int i = 1; i < N; i++) { //连续追加 N - 1次，所以一共重复了N次
            result += replaceChar;
        }
        return result;
    }


    public static void main(String[] args) {


        KeyWordDFA keyWordDFA = new KeyWordDFA();
        keyWordDFA.addKeyword("武装份子的妈妈");
        keyWordDFA.addKeyword("武装份子的");
        keyWordDFA.addKeyword("武装份子");
        keyWordDFA.addKeyword("武装份");

        System.out.println(keyWordDFA.searchKeyword2("武装份子的爱武装份子的妈妈", MatchType.MAX_LEN_MATCH));
        System.out.println(keyWordDFA.searchKeyword2("武装份子的爱", MatchType.MIN_LEN_MATCH));

        System.out.println(keyWordDFA.searchKeyword("武装份子的爱", MatchType.MAX_LEN_MATCH));

        System.out.println(keyWordDFA.replaceKeyword("武装份子的爱武装份子的妈妈", MatchType.MIN_LEN_MATCH, "*", ReplaceType.MAX_FIRST));

        System.out.println(keyWordDFA.replaceKeyword("武装份子的爱武装份子的妈妈", MatchType.MAX_LEN_MATCH, "*", ReplaceType.MIN_FIRST));
        System.out.println(keyWordDFA.replaceKeyword("武装份子的爱武装份子的妈妈", MatchType.MAX_LEN_MATCH, "*", ReplaceType.MAX_FIRST));
        System.out.println(keyWordDFA.replaceKeyword("武装份子的爱武装份子的妈妈", MatchType.MAX_LEN_MATCH, "*", null));
    }






    @Test
    public void test() throws IOException {

        KeyWordDFA keyWordDFA = new KeyWordDFA();
        List<String> stringList = IOUtils.readLines(new FileInputStream(new File("F:/485.txt")), "GBK");
        List<String> temp = new ArrayList<>();


        stringList.forEach(i -> {
            i = i.trim();
            if (i.length() == 0) {
                return;
            }
            Stream<String> stream =
            Arrays.stream(i.split("\\s+"))
                            .map(a -> a.split("。")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("，")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("？")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("！")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("”")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("“")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("”")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("“")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("\\.")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("\\:")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split(",")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("\"")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("\'")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("；")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("：")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("）")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            stream = stream.map(a -> a.split("（")).flatMap(array -> Arrays.stream(array).map(u -> u.trim()));
            List<String> collect = stream.filter(ele -> ele.length() > 0).collect(Collectors.toList());
            for (String ele : collect) {
                String[] strings = null;
                if (ele.length() % 4 == 0) {
                    strings = StringUtils.stringSplit(ele, 4);
                } else if(ele.length() % 4 == 1){
                    strings = StringUtils.stringSplit(ele, 7);
                }
                else if(ele.length() % 4 == 2){
                    strings = StringUtils.stringSplit(ele, 4);
                }
                else if(ele.length() % 4 == 3){
                    strings = StringUtils.stringSplit(ele, 3);
                }
                for (String s : strings) {
                    temp.add(s);
                }
            }

        });

        Collections.sort(temp, (a, b) -> a.compareTo(b) * -1);
        temp.forEach(i -> keyWordDFA.addKeyword(i));

        System.out.println(keyWordDFA.searchKeyword("萧峰", MatchType.MAX_LEN_MATCH));
        System.out.println(keyWordDFA.searchKeyword("萧大侠", MatchType.MAX_LEN_MATCH));
        System.out.println(keyWordDFA.searchKeyword("萧帮主", MatchType.MAX_LEN_MATCH));
        System.out.println(keyWordDFA.searchKeyword("契丹人", MatchType.MAX_LEN_MATCH));
        System.out.println(keyWordDFA.searchKeyword("大理段氏必将竭尽全力的保护大家的安全", MatchType.MAX_LEN_MATCH));
        System.out.println(keyWordDFA.searchKeyword("阿朱笑了起来", MatchType.MAX_LEN_MATCH));
        System.out.println(keyWordDFA.searchKeyword("白长老情义深重", MatchType.MAX_LEN_MATCH));
    }

}
