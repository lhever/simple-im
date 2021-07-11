package com.lhever.common.core.support.parser.token;

public class NoopTokenParser extends AbstractTokenParser {

    public NoopTokenParser() {
        super();
    }

    public NoopTokenParser(ParserConverter converter) {
        this(converter, null);
    }

    public NoopTokenParser(ParserConverter converter, Object ifNull) {
        super(converter);
        setIfNull(ifNull);
    }

    public NoopTokenParser(Object ifNull) {
        this(null, ifNull);
    }


    @Override
    public String handleToken(String token) {
        return token;
    }
}
