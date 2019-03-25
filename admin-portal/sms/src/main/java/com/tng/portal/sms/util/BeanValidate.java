package com.tng.portal.sms.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Nick_Xiong
 *
 */
public class BeanValidate {

	private static Logger logger = LoggerFactory.getLogger(BeanValidate.class);

	/**
	 * Validate the fields one by one
	 * @param obj
	 * @param clazz
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public  static <T> void validateFields(Object obj, Class<T> clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(null == obj) {
			return;
		}
		Field[] fields = clazz.getDeclaredFields();
        List<Field> needValidateFields = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(FieldRules.class)) {
            	needValidateFields.add(field);
            }
        }
        FieldRules anno = null;
        for(Field f: needValidateFields) {
        	anno = f.getAnnotation(FieldRules.class);
        	boolean nullable = anno.nullable();
        	boolean isNumeric = anno.isNumeric();
        	String format = anno.format();
        	String fieldName = f.getName();
        	Object fieldValue = PropertyUtils.getProperty(obj, fieldName);
        	if(!nullable) {
        		if (null == fieldValue) {
        			throw new IllegalArgumentException(fieldName + " can't be null");
        		} else if(fieldValue instanceof String  && StringUtils.isBlank((String)fieldValue)) {
        			throw new IllegalArgumentException(fieldName + "it can't be null");//sonar modify 
        		}
        	}
        
           if(isNumeric) {
        	   if(!(fieldValue instanceof String && StringUtils.isNumeric((String)fieldValue))) {
            	   throw new IllegalArgumentException(fieldName + " is not numeric");
               }  
           }
           
           
           if (!"".equals(format)) {
               if ((fieldValue instanceof String)) {
            	   DateFormat df = new SimpleDateFormat(format);
            	   try {
					df.parse((String)fieldValue);
				} catch (ParseException e) {
					logger.error("format:{}",format,e);
					throw new IllegalArgumentException(fieldName + "format is not suit to" + format);
				}
                   
               }
           }
        	
        }
		
	}
	
	/**
	 * Validate the complex logic
	 * @param obj
	 * @param clazz
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public  static <T> void validateObject(Object obj, Class<T> clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(null == obj) {
			return;
		}
		boolean flag = clazz.isAnnotationPresent(ClassValidator.class);
		if(flag) {
			ClassValidator anno = clazz.getAnnotation(ClassValidator.class);
			String validateMethod = anno.validatMethod();
			MethodUtils.invokeMethod(obj, validateMethod, null);
		}
	}
}
