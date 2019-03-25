package com.tng.portal.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by Ivan_Stankov on 28.04.2015.
 */
public class EnumOrdinalSerializer extends JsonSerializer<Enum> {
    @Override
    public void serialize(Enum anEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(anEnum.ordinal());
    }
}
