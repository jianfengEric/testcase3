package com.tng.portal.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serializer used for {@link Number} type with null value and write number as "0"
 *
 * @author Anna_Popova
 */

public class NumberJsonNullSerializer extends JsonSerializer<Number> {

    public static final JsonSerializer INSTANCE = new NumberJsonNullSerializer();

    private NumberJsonNullSerializer() {
    }

    @Override
    public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeNumber(0);
    }
}
