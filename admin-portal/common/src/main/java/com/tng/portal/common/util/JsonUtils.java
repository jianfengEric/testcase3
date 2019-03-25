package com.tng.portal.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Zero on 2016/11/8.
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper objectMapper;
    
    private JsonUtils(){}

    /**
     *  Using generic methods json String conversion to corresponding JavaBean Object. 
     * (1) Converted to ordinary JavaBean;readValue(json,Student.class)
     * (2) Convert to List, as List<Student>, Pass second parameters to Student
     * [].class. Then use it Arrays.asList(); Method converts the resulting array to a specific type. List
     *
     * @param jsonStr
     * @param valueType
     * @return
     */
    public static <T> T readValue(String jsonStr, Class<T> valueType) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            logger.error("jsonStr:{} valueType:{}",jsonStr,valueType,e);
        }

        return null;
    }

    /**
     *  hold JavaBean Convert to json Character string 
     *
     * @param object
     * @return
     */
    public static String toJSon(Object object) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("object:{} ",object,e);
        }

        return null;
    }
}
