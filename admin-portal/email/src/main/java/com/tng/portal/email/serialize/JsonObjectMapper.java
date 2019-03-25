package com.tng.portal.email.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This mapper provides functionality for
 * converting between Java objects and matching JSON constructs.
 *
 * Mapper registered custom {@link JsonSerializerProvider}
 *
 * @author Anna_Popova
 */

public class JsonObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1687448143760034558L;

    public JsonObjectMapper() {
        super();
        this.setSerializerProvider(new JsonSerializerProvider());
    }
}
