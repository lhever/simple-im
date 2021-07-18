package com.lhever.common.core.support.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.lhever.common.core.utils.DateFormatUtils;

import java.io.IOException;
import java.util.Date;

public class CustomDateSerializer extends DateSerializer {
    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(DateFormatUtils.toISO8601DateString(value));
    }
}