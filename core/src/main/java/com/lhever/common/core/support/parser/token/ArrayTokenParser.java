package com.lhever.common.core.support.parser.token;


public class ArrayTokenParser extends AbstractTokenParser {
    private Object[] array;
    private int index = 0;

    public ArrayTokenParser(Object[] array) {
        super();
        this.array = array;
    }

    public ArrayTokenParser(Object[] array, ParserConverter converter) {
        this(array, converter, null);
    }

    public ArrayTokenParser(Object[] array, Object ifNull) {
        this(array, null, ifNull);
    }

    public ArrayTokenParser(Object[] array, ParserConverter converter, Object ifNull) {
        super(converter);
        this.array = array;
        setIfNull(ifNull);
    }

    @Override
    public String handleToken(String token) {
        if (array == null || array.length == 0) {
            return null;
        }

        if (index >= array.length) {
            return null;
        }
        Object e = array[index];
        index++;

        if (e == null) {
            return null;
        }

        return e.toString();
    }
}
