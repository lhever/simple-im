package com.lhever.common.core.utils;


import com.lhever.common.core.consts.CommonConsts;

import java.util.Collection;
import java.util.List;

/**
 * @author lihong10
 * @creatTime 2018/8/10 20:39
 */
public class SqlUtils {
    public static final Integer LENGTH = 999;// 每个分段的长度，对于Oracle，一般设置为800-1000
    /**
     * 转义特殊字符, 该方法仅用于Postgres数据库，若用于其他关系型数据库，请新增方法 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return
     * @author lihong10 2019/1/4 11:34
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/4 11:34
     * @modify by reason:{原因}
     */
    public static String escapeForPostgres(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return keyword;
        }
        if (keyword.contains("\\")) {
            keyword = keyword.replaceAll("\\\\", "\\\\\\\\");
        }

        String[] fbsArr = {"\"", "/", "%", "_", "\'"};
        for (String key : fbsArr) {
            if (keyword.contains(key)) {
                keyword = keyword.replaceAll(key, "\\\\" + key);
            }
        }


        return keyword;
    }


    public static String like(String keyword) {
        return CommonConsts.PERCENT + keyword + CommonConsts.PERCENT;
    }

    public static String escapedLike(String keyword) {
        return CommonConsts.PERCENT + escapeForPostgres(keyword) + CommonConsts.PERCENT;
    }

    public static String tailLike(String keyword) {
        return keyword + CommonConsts.PERCENT;
    }

    public static String escapedTailLike(String keyword) {
        return escapeForPostgres(keyword) + CommonConsts.PERCENT;
    }

    public static String headLike(String keyword) {
        return CommonConsts.PERCENT + keyword;
    }

    public static String escapedHeadLike(String keyword) {
        return CommonConsts.PERCENT + escapeForPostgres(keyword);
    }





    /**
     * 拼接字符串
     *
     * @param words
     * @return
     * @author lihong10 2019/1/4 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/4 12:07
     * @modify by reason:{原因}
     */
    public static String concat(String... words) {
        if (words == null) {
            return CommonConsts.EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word != null) { //此处不适合采用StringUtils.isNotBlank判空
                builder.append(word);
            }
        }
        return builder.toString();
    }
    /**
     * 返回的SQL语句片段：(XX IN (.......) OR XX IN (......))这样的形式
     * @author jinbenbin 2013-11-27 下午03:39:42
     * @param paramName 需要进行in查询的查询参数的名称
     * @param paramsCollection 待拆分的参数列表，类型支持Integer、String
     * @return 拼接的SQL语句片段
     */
    public static <T> String getSQLIn(String paramName, Collection<T> paramsCollection) {
        if (paramName == null || paramsCollection == null || paramsCollection.size() == 0)
            return null;
        List<List<T>> list = CollectionUtils.splitCollection(paramsCollection,LENGTH);
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append("OR ");
            }
            // 如果是String得加上单引号
            sb.append(paramName).append(" IN (").append(listToString(list.get(i))).append(") ");
        }
        return (size > 1) ? new StringBuilder().append("(").append(sb).append(")").toString() : sb.toString();
    }

    /**
     * 将列表中的元素连成一个以逗号分隔的字符串，类型支持Integer、String
     * @author jinbenbin 2013-11-27 下午04:29:39
     */
    private static <T> String listToString(List<T> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && !list.isEmpty()) {
            T object = list.get(0);
            if (object instanceof String) {
                sb.append("'");
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        sb.append("','");
                    }
                    sb.append(list.get(i));
                }
                sb.append("'");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        sb.append(",");
                    }
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        System.out.println(escapeForPostgres("\\"));
        System.out.println(escapeForPostgres("\\"));
        System.out.println(escapeForPostgres("/"));
        System.out.println(escapeForPostgres("%"));
        System.out.println(escapeForPostgres("_"));
    }
}
