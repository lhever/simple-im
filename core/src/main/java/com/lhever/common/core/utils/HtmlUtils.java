package com.lhever.common.core.utils;

public class HtmlUtils {
    /**
     * 邮件标签转义工具
     *
     * @param content
     * @return
     * @author sunbo14 2018/12/18 18:58
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/18 18:58
     * @modify by reason:{原因}
     */
    public static String replace(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        content = content.replaceAll("<", "&lt;");
        content = content.replaceAll(">", "&gt;");
        return content;
    }
}
