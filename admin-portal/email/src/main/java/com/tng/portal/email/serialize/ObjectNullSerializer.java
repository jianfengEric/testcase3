package com.tng.portal.email.serialize;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Null serializer for object types. Writes "{}" to JSON (empty object)
 *
 * @author Igor_Zhukov
 * @since 2015-07-04
 */
public class ObjectNullSerializer extends JsonSerializer<Object> {
    private static final ObjectNullSerializer INSTANCE = new ObjectNullSerializer();

    public static ObjectNullSerializer getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();
    }
}
