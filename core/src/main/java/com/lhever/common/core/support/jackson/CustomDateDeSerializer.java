package com.lhever.common.core.support.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.lhever.common.core.utils.DateFormatUtils;

import java.io.IOException;
import java.util.Date;

public class CustomDateDeSerializer extends DateDeserializers.DateDeserializer {
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p != null) {
            String rawDateStr = p.getText();

            Date date = DateFormatUtils.toDate(rawDateStr);
            if (date != null) {
                return date;
            }
        }
        return super.deserialize(p, ctxt);
    }
}
