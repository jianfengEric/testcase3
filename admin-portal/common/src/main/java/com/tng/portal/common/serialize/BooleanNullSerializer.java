package com.tng.portal.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serializer used for {@link Boolean} type with null value and writing result as "false"
 *
 * @author Anna_Popova
 */
public class BooleanNullSerializer extends JsonSerializer<Boolean> {

    public static final JsonSerializer INSTANCE = new BooleanNullSerializer();

    private BooleanNullSerializer() {
    }

    @Override
    public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeBoolean(Boolean.FALSE);
    }
}
