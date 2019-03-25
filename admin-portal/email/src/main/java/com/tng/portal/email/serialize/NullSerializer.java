package com.tng.portal.email.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * This is the special serializer for null values.
 * It writing strings as ""
 *
 * @author Anna_Popova
 */
public class NullSerializer extends JsonSerializer<Object> {

    public static final JsonSerializer INSTANCE = new NullSerializer();

    private NullSerializer() {
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString("");
    }
}
