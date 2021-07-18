package com.lhever.common.core.support.initializer.wrapper;

public class StringBuilderWrapper {
    private static final String LINE_RETURN = "\n";
    private StringBuilder stringBuilder = null;

    public StringBuilderWrapper(StringBuilder builder) {
        this.stringBuilder = builder;
    }

    public StringBuilderWrapper() {
        this.stringBuilder = new StringBuilder();
    }

    public StringBuilderWrapper appendln(String str) {
        stringBuilder.append(str).append(LINE_RETURN);
        return this;
    }

    public StringBuilderWrapper append(String str) {
        stringBuilder.append(str);
        return this;
    }


    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public StringBuilder builder() {
        return stringBuilder;
    }


}
