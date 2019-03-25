package com.tng.portal.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Null serializer for collection and array types. Writes "[]" to JSON (empty array)
 *
 * @author Igor_Zhukov
 * @since 2015-07-04
 */
public class CollectionNullSerializer extends JsonSerializer<Object> {
    private static final CollectionNullSerializer INSTANCE = new CollectionNullSerializer();

    public static CollectionNullSerializer getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeEndArray();
    }
}
