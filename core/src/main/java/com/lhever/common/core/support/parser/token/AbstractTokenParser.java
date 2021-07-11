package com.lhever.common.core.support.parser.token;

import com.lhever.common.core.consts.CommonConsts;

public abstract class AbstractTokenParser {

    private Object ifNull;
    private ParserConverter converter;


    public void setIfNull(Object ifNull) {
        this.ifNull = ifNull;
    }

    public void setConverter(ParserConverter converter) {
        this.converter = converter;
    }

    public AbstractTokenParser(ParserConverter converter) {
        this();
        this.converter = converter;
    }

    public AbstractTokenParser() {
        ifNull = CommonConsts.EMPTY;
    }

    /**
     * 将字符串text中由openToken(目前默认是{)和closeToken（目前默认是}）组成的占位符依次替换为特定的值。
     * 该方法取自mybatis框架，是mybatis默认的占位符解析器，性能极高。
     *
     * @param openToken
     * @param closeToken
     * @param text
     * @return
     * @author lihong10 2019/1/11 16:19
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/11 16:19
     * @modify by reason:{原因}
     */
    public String parse(String openToken, String closeToken, String text) {
        if (text == null || text.isEmpty()) {
            return CommonConsts.EMPTY;
        }
        // search open token
        int start = text.indexOf(openToken, 0);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    String value = convert(handleToken(expression.toString()));
                    //此处不适合使用StringUtils.isBlank方法判断字符串是否为空
                    if (value == null && ifNull != null) {
                        value = ifNull.toString();
                    }
                    builder.append(value);
                    offset = end + closeToken.length();
                }
            }
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }

    public abstract String handleToken(String token);


    protected String convert(String handled) {
        if (converter != null) {
            handled = converter.convert(handled);
        }
        return handled;
    }

}
