package com.lhever.common.core.utils;

import com.lhever.common.core.support.parser.token.AbstractTokenParser;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/4/9 15:08
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/9 15:08
 * @modify by reason:{方法名}:{原因}
 */
public class MdUtils {

    private static String start = "@[";
    private static String end = "]";



    public static String parseURL(AbstractTokenParser processor, String md) {
        String processed = ParseUtils.doParse(processor, start, end, md);
        return processed;
    }



}
