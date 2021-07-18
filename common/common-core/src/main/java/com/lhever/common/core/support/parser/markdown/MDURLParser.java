package com.lhever.common.core.support.parser.markdown;

import com.lhever.common.core.utils.StringUtils;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/4/9 10:57
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/9 10:57
 * @modify by reason:{方法名}:{原因}
 */
public abstract class MDURLParser {

    protected String startToken;
    protected String middleToken;
    protected String endToken;

    public MDURLParser(String startToken, String middleToken, String endToken) {
        this.startToken = startToken;
        this.middleToken = middleToken;
        this.endToken = endToken;
    }

    public String parseUrl(String text, int offset) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        final StringBuilder builder = new StringBuilder();
        char[] chars = text.toCharArray();

        StringBuilder url = null;
        StringBuilder title = null;

        while (offset < text.length()) {

            int startIndex = text.indexOf(startToken, offset);
            if (isNotFind(startIndex)) {
                builder.append(chars, offset, chars.length - offset);
                offset = chars.length;
                break;
            }

            int middleIndex = text.indexOf(middleToken, startIndex + startToken.length());
            if (isNotFind(middleIndex)) {
                builder.append(chars, offset, chars.length - offset);
                offset = chars.length;
                break;
            }

            int endIndex = text.indexOf(endToken, middleIndex + middleToken.length());
            if (isNotFind(endIndex)) {
                builder.append(chars, offset, chars.length - offset);
                offset = chars.length;
                break;
            }

            String substring = text.substring(startIndex + startToken.length(), middleIndex);

            int i = substring.lastIndexOf(startToken);

            if (i > -1) {
                startIndex = startIndex + startToken.length() + i;
            }

            if (url == null) {
                url = new StringBuilder();
            } else {
                url.setLength(0);
            }

            if (title == null) {
                title = new StringBuilder();
            } else {
                title.setLength(0);
            }

            int afterMiddle = middleIndex + middleToken.length();
            int afterStart = startIndex + startToken.length();

            builder.append(chars, offset, afterMiddle - offset);

            StringBuilder titleBuilder = title.append(chars, afterStart, middleIndex - afterStart);
            StringBuilder urlBuilder = url.append(chars, afterMiddle, endIndex - afterMiddle);

            String handled = handle(text, offset, startIndex, middleIndex, endIndex, titleBuilder.toString(), urlBuilder.toString());
            builder.append(handled);

            int afterEnd = endIndex + endToken.length();

            builder.append(chars, endIndex, endToken.length());

            offset = afterEnd;
        }
        return builder.toString();
    }

    private static boolean isNotFind(int index) {
        return !isFind(index);
    }

    private static boolean isFind(int index) {
        return index > -1;
    }

    public abstract String handle(String text, int offset, int startIndex, int middleIndex, int endIndex, String title, String url);


}
