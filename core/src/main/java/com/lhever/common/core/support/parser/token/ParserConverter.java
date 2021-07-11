package com.lhever.common.core.support.parser.token;

@FunctionalInterface
public interface ParserConverter {

    public String convert(String token);
}
