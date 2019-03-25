package com.tng.portal.common.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zero on 2016/12/29.
 */
public class DateJsonDeserialzer extends JsonDeserializer<Date> {
	
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String txt = jsonParser.getText();
        if(null!=txt){
            try {
                return sdf.parse(txt);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
}
