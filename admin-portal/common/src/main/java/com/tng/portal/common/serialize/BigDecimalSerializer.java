package com.tng.portal.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Serializer used for {@link Boolean} type with null value and writing result as "false"
 *
 * @author Anna_Popova
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    private BigDecimalSerializer() {
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    	jgen.writeString(value.stripTrailingZeros().toPlainString());
    }
}
