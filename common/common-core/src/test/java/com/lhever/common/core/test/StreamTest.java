package com.lhever.common.core.test;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.support.tuple.TwoPair;
import com.lhever.common.core.utils.JsonUtils;
import com.lhever.common.core.utils.StringUtils;
import lombok.Data;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/11/29 16:08
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/11/29 16:08
 * @modify by reason:{方法名}:{原因}
 */
public class StreamTest {

    @Data
    public static class BlogPost {
        private String title;
        private String author;
        private Integer type;
        private Integer count;

        public BlogPost(String title, String author, Integer type, Integer count) {
            this.title = title;
            this.author = author;
            this.type = type;
            this.count = count;
        }

    }

    public enum  ArticleType {
        NEWS,
        REVIEW,
        GUIDE
    }


    @Data
    public static class Article {
        String title;
        String author;
        ArticleType type;
        int likes;

        public Article(String title, String author, ArticleType type, int likes) {
            this.title = title;
            this.author = author;
            this.type = type;
            this.likes = likes;
        }

        public Article() {
        }
    }

   /* 分组结果的键为复杂类型
      分类函数不仅限于返回一个纯量或字符串，分类结果的键可以是任何类型，但是要求实现了必要的equals和hashcode方法。
    */
    @Test
    public void testKeyIsComplicated() {

        List<Article> articles = Arrays.asList(
                new Article("财富秘密", "周哈哈", ArticleType.GUIDE, 100),
                new Article("易经", "李红", ArticleType.GUIDE, 100),
                new Article("中国地理", "李红", ArticleType.GUIDE, 100),
                new Article("太阳城", "李红", ArticleType.GUIDE, 100),
                new Article("一天三", "周哈哈", ArticleType.GUIDE, 100));

        Map<TwoPair<String, ArticleType>, List<Article>> complexKeMap = articles.stream()
                .collect(Collectors.groupingBy(new Function<Article, TwoPair<String, ArticleType>>() {
                    @Override
                    public TwoPair<String, ArticleType> apply(Article article) {
                        return new TwoPair(article.getAuthor(), article.getType());
                    }
                }));

        System.out.println(JsonUtils.object2Json(complexKeMap, true));
    }


    @Test
    public void groupingByConvertResultTest0() {
        List<BlogPost> blogPostList = new ArrayList();
        blogPostList.add(new BlogPost("post1", "zhuoli", 1, 30));
        blogPostList.add(new BlogPost("post2", "zhuoli", 1, 40));
        blogPostList.add(new BlogPost("post3", "zhuoli", 2, 15));
        blogPostList.add(new BlogPost("post4", "zhuoli", 3, 33));
        blogPostList.add(new BlogPost("post5", "Alice", 1, 99));
        blogPostList.add(new BlogPost("post6", "Michael", 3, 65));

        Map<Integer, List<BlogPost>> collect = blogPostList.stream()
                .collect(Collectors.groupingBy(BlogPost::getType));
        System.out.println(JsonUtils.object2Json(collect, true));
    }



    @Test
    public void groupingByConvertResultTest() {
        List<BlogPost> blogPostList = new ArrayList();
        blogPostList.add(new BlogPost("post1", "zhuoli", 1, 30));
        blogPostList.add(new BlogPost("post2", "zhuoli", 1, 40));
        blogPostList.add(new BlogPost("post3", "zhuoli", 2, 15));
        blogPostList.add(new BlogPost("post4", "zhuoli", 3, 33));
        blogPostList.add(new BlogPost("post5", "Alice", 1, 99));
        blogPostList.add(new BlogPost("post6", "Michael", 3, 65));

        Map<Integer, String> postsPerType = blogPostList.stream()
                .collect(Collectors.groupingBy(BlogPost::getType,
                        Collectors.mapping(BlogPost::getTitle, Collectors.joining(", ", "Post titles: [", "]"))));
        System.out.println(postsPerType);
    }

    @Test
    public void groupingByConvertResultTest_1() {
        List<BlogPost> blogPostList = new ArrayList();
        blogPostList.add(new BlogPost("post1", "zhuoli", 1, 30));
        blogPostList.add(new BlogPost("post2", "zhuoli", 1, 40));
        blogPostList.add(new BlogPost("post3", "zhuoli", 2, 15));
        blogPostList.add(new BlogPost("post4", "zhuoli", 3, 33));
        blogPostList.add(new BlogPost("post5", "Alice", 1, 99));
        blogPostList.add(new BlogPost("post6", "Michael", 3, 65));

        Map<Integer, List<String>> postsPerType = blogPostList.stream()
                .collect(Collectors.groupingBy(BlogPost::getType,
                        Collectors.mapping(BlogPost::getTitle, Collectors.toCollection(ArrayList::new))));
        System.out.println(postsPerType);
    }



    @Test
    public void groupingByMultiItemTest(){
        List<BlogPost> blogPostList = new ArrayList();
        blogPostList.add(new BlogPost("post1", "zhuoli", 1, 30));
        blogPostList.add(new BlogPost("post2", "zhuoli", 1, 40));
        blogPostList.add(new BlogPost("post3", "zhuoli", 2, 15));
        blogPostList.add(new BlogPost("post4", "zhuoli", 3, 33));
        blogPostList.add(new BlogPost("post5", "Alice", 1, 99));
        blogPostList.add(new BlogPost("post6", "Michael", 3, 65));

        Map<String, Map<Integer, List<BlogPost>>> map = blogPostList.stream()
                .collect(Collectors.groupingBy(BlogPost::getAuthor, Collectors.groupingBy(BlogPost::getType)));
        System.out.println(JsonUtils.object2Json(map));
        System.out.println(map);

    }


    @Test
    public void test() {
        StringBuilder builder = new StringBuilder();
        builder.append((Object) null);
        System.out.println(builder.toString());

        Properties prop = new Properties();
        prop.getProperty("lalal");

        System.out.println(Arrays.toString("aaa".split("\\.")));
        System.out.println(StringUtils.splitToList("aaa", CommonConsts.DOT));


        List<String> names = Arrays.stream(new File("D:/").listFiles()).map(f -> f.getName()).collect(Collectors.toList());

        System.out.println(StringUtils.join(names, "\n\r"));

        System.out.println("----------------------------");


        System.out.println(StringUtils.join(new File("D:/").list(), "\n\r"));







    }

}
