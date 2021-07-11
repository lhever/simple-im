package com.lhever.common.core.support.parser.token;

import java.util.Properties;

public class PropertiesTokenParser extends AbstractTokenParser {
    private Properties prop;

    public PropertiesTokenParser(Properties prop) {
        super();
        this.prop = prop;
    }

    public PropertiesTokenParser(Properties prop, ParserConverter converter, Object ifNull) {
        super(converter);
        this.prop = prop;
        setIfNull(ifNull);
    }

    public PropertiesTokenParser(Properties prop, ParserConverter converter) {
        this(prop, converter, null);
    }

    public PropertiesTokenParser(Properties prop, Object ifNull) {
        this(prop, null, ifNull);
    }

    @Override
    public String handleToken(String token) {
        if (prop == null) {
            return null;
        }
        if (prop.containsKey(token)) {
            return prop.getProperty(token);
        }

        return null;
    }

}
