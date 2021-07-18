package com.lhever.common.core.support.parser.token;

import java.util.List;

public class ListTokenParser<E> extends AbstractTokenParser {

    private List<E> list;
    private int index = 0;

    public ListTokenParser(List<E> list) {
        super();
        this.list = list;
    }

    public ListTokenParser(List<E> list, ParserConverter converter) {
        this(list, converter, null);
    }

    public ListTokenParser(List<E> list, ParserConverter converter, Object ifNull) {
        super(converter);
        this.list = list;
        setIfNull(ifNull);
    }

    public ListTokenParser(List<E> list, Object ifNull) {
        this(list, null, ifNull);
    }

    @Override
    public String handleToken(String token) {
        if (list == null || list.size() == 0) {
            return null;
        }

        if (index >= list.size()) {
            return null;
        }
        E e = list.get(index);
        //索引往前移动一步
        index++;

        if (e == null) {
            return null;
        }
        return e.toString();

    }
}
