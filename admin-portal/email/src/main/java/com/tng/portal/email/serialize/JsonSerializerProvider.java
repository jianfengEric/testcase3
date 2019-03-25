package com.tng.portal.email.serialize;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonSerializerProvider is looking for that type of customize null serializer.
 * It use one that is class sensitive, writing default value instead null
 *
 * @author Anna_Popova
 */

public class JsonSerializerProvider extends DefaultSerializerProvider {

    private static final long serialVersionUID = 486789129234532383L;

    private static Map<Class<?>, JsonSerializer> serializers = new HashMap<>();

    static {
        serializers.put(Integer.class, NumberJsonNullSerializer.INSTANCE);
        serializers.put(Double.class, NumberJsonNullSerializer.INSTANCE);
        serializers.put(Long.class, NumberJsonNullSerializer.INSTANCE);
        serializers.put(BigDecimal.class, NumberJsonNullSerializer.INSTANCE);
        serializers.put(Boolean.class, BooleanNullSerializer.INSTANCE);
        serializers.put(String.class, NullSerializer.INSTANCE);
    }

    public JsonSerializerProvider() {
        super();
    }

    public JsonSerializerProvider(JsonSerializerProvider provider, SerializationConfig config, SerializerFactory jsf) {
        super(provider, config, jsf);
    }

    @Override
    public JsonSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new JsonSerializerProvider(this, config, jsf);
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonSerializer findNullValueSerializer(BeanProperty property) throws JsonMappingException {

        JsonSerializer jsonSerializer = serializers.get(property.getType().getRawClass());
        if (jsonSerializer == null) {
            if (property.getType().isArrayType() || property.getType().isCollectionLikeType()) {
                jsonSerializer = CollectionNullSerializer.getInstance();
            } else if (property.getType().isEnumType()) {
                jsonSerializer = NullSerializer.INSTANCE;
            } else {
                jsonSerializer = ObjectNullSerializer.getInstance();
            }
        }
        return jsonSerializer;
    }
}