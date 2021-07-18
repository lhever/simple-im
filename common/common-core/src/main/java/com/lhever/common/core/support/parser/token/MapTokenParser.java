package com.lhever.common.core.support.parser.token;

import java.util.Map;

public class MapTokenParser<V> extends AbstractTokenParser {
    private Map<String, V> map = null;

    public MapTokenParser(Map<String, V> map) {
        super();
        this.map = map;
    }

    public MapTokenParser(Map<String, V> map, ParserConverter converter, Object ifNull) {
        super(converter);
        this.map = map;
        setIfNull(ifNull);
    }

    public MapTokenParser(Map<String, V> map, ParserConverter converter) {
        this(map, converter, null);
    }

    public MapTokenParser(Map<String, V> map, Object ifNull) {
        this(map, null, ifNull);
    }

    @Override
    public String handleToken(String token) {

        if (map == null) {
            return null;
        }

        V v = map.get(token);
        if (v == null) {
            return null;
        }

        return v.toString();
    }
}
