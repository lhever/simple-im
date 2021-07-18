package com.lhever.common.core.support.parser.token;

import com.lhever.common.core.utils.Bean2MapUtil;

public class BeanTokenParser extends AbstractTokenParser {
    private Object obj;

    public BeanTokenParser(Object obj) {
        super();
        this.obj = obj;
    }

    public BeanTokenParser(Object obj, ParserConverter converter, Object ifNull) {
        super(converter);
        this.obj = obj;
        setIfNull(ifNull);
    }

    public BeanTokenParser(Object obj, ParserConverter converter) {
        this(obj, converter, null);
    }

    public BeanTokenParser(Object obj, Object ifNull) {
        this(obj, null, ifNull);
    }

    @Override
    public String handleToken(String token) {

        if (obj == null) {
            return null;
        }
        Object field = Bean2MapUtil.getProperty(obj, token);
        if (field == null) {
            return null;
        }
        return field.toString();
    }
}
