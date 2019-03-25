package com.tng.portal.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zero on 2016/12/29.
 */
public class DateJsonSerializer extends JsonSerializer<Date>{
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(sdf.format(date));
    }
}
