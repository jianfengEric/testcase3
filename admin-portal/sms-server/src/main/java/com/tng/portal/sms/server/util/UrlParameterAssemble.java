package com.tng.portal.sms.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tng.portal.common.constant.DateCode;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;



public class UrlParameterAssemble {
	
	private static String dateToString(Date date, String formatStr)
	  {
	    if (date == null) {
	      return null;
	    }
	    if ((formatStr == null) || (formatStr.equals(""))) {
	      formatStr = DateCode.dateFormatSs;
	    }
	    DateFormat sdf = new java.text.SimpleDateFormat(formatStr);
	    return sdf.format(date);
	  }
	
	
	public  static String assembleGetQueryStr(Map<String,String> map) {
		StringBuffer queryStr = new StringBuffer();
		int count = 1;
		for (Object key : map.keySet()) {
			queryStr.append(key + "=" + map.get(key));
			if (count != map.size()) {
				queryStr.append("&");
			}
			count++;
		}
		return queryStr.toString();
	}

	public  static <T> String assembleGetQueryStr(Object obj, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		StringBuffer queryStr = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();
        List<Field> displayFields = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(UrlParameter.class)) {
            	displayFields.add(field);
            }
        }
        
        Collections.sort(displayFields, new FieldComparator());
        
        UrlParameter  anno = null;
        int count = 1;
        for(Field f: displayFields) {
        	 if (f.isAnnotationPresent(UrlParameter.class)) {
        		 anno = f.getAnnotation(UrlParameter.class);
        		 String name = anno.name();
        		 String map = anno.map();
        		 String format = anno.dateFormat();
        		 String formatterMethod = anno.formatterMethod();
        		 String fieldName = f.getName();
             	 Object fieldValue = PropertyUtils.getProperty(obj, fieldName);
                 if (!"".equals(map)) {
                     String[] strArr = map.split(",");
                     Map<String, String> strMap = new HashMap<>();
                     for (String str : strArr) {
                         strMap.put(str.substring(0, str.indexOf('=')), str.substring(str.indexOf('=') + 1));
                     }
                     queryStr.append(name + "=" + strMap.get(fieldValue));
                 } else if (!"".equals(format)) {
                     if ((fieldValue instanceof Date)) {
                         Date date = (Date) fieldValue;
                         String dateStr = dateToString(date, format);
                         queryStr.append(name + "=" + dateStr );
                         
                     }
                 } else if (!"".equals(formatterMethod)) {
                	 Class<?> modelClass = obj.getClass();
                	 Class fieldClazz = PropertyUtils.getPropertyType(obj, fieldName);
                	 Method method = modelClass.getMethod(formatterMethod, fieldClazz);
                     Object formattedValue = method.invoke(obj, fieldValue);
                     queryStr.append(name + "=" + formattedValue );
                 } else {
                	 String parameterValue = "";
                	 if (null != fieldValue) {
                		 parameterValue = fieldValue.toString();
                	 }
                	 queryStr.append(name + "=" + parameterValue);
                 }
                 if(count != displayFields.size()) {
                	 queryStr.append("&");
                 }
        	 }
        	 count ++;
        }
       
		
		return queryStr.toString();
	}
		
	public  static <T> Map assembleQueryMap(Object obj, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Map mapResult = new HashMap();
		Field[] fields = clazz.getDeclaredFields();
        List<Field> displayFields = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(UrlParameter.class)) {
            	displayFields.add(field);
            }
        }
        
        Collections.sort(displayFields, new FieldComparator());
        
        UrlParameter  anno = null;
        for(Field f: displayFields) {
        	 if (f.isAnnotationPresent(UrlParameter.class)) {
        		 anno = f.getAnnotation(UrlParameter.class);
        		 String name = anno.name();
        		 String map = anno.map();
        		 String format = anno.dateFormat();
        		 String formatterMethod = anno.formatterMethod();
        		 String fieldName = f.getName();
             	 Object fieldValue = PropertyUtils.getProperty(obj, fieldName);
             	
                 if (!"".equals(map)) {
                     String[] strArr = map.split(",");
                     Map<String, String> strMap = new HashMap<>();
                     for (String str : strArr) {
                         strMap.put(str.substring(0, str.indexOf('=')), str.substring(str.indexOf('=') + 1));
                     }
                     mapResult.put(name, strMap.get(fieldValue));
                 } else if (!"".equals(format)) {
                     if ((fieldValue instanceof Date)) {
                         Date date = (Date) fieldValue;
                         String dateStr = dateToString(date, format);
                         mapResult.put(name, dateStr);
                         
                     }
                 } else if (!"".equals(formatterMethod)) {
                	 Class<?> modelClass = obj.getClass();
                	 Class fieldClazz = PropertyUtils.getPropertyType(obj, fieldName);
                	 Method method = modelClass.getMethod(formatterMethod, fieldClazz);
                     Object formattedValue = method.invoke(obj, fieldValue);
                     mapResult.put(name, formattedValue);
                 } else {
                	 String parameterValue = "";
                	 if (null != fieldValue) {
                		 parameterValue = fieldValue.toString();
                	 }
                	 mapResult.put(name, parameterValue);
                 }
                 
        	 }
        	
        }
		
		return mapResult;
	}
	
	public static <T> String assembleString(Object obj, Class<T> clazz,String prefix) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		StringBuffer stringResult = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();
        List<Field> displayFields = new ArrayList<>();
        UrlParameter  anno = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(UrlParameter.class)) {
            	anno = field.getAnnotation(UrlParameter.class);
            	String condition = anno.condition();
            	boolean showFlag = executeCondition(obj, clazz, condition);
    			if (!showFlag) {
    				continue;
    			}
            	displayFields.add(field);
            }
        }
        
        Collections.sort(displayFields, new FieldComparator());
        
        boolean first = true;
        for(Field f: displayFields) {
        	 if (f.isAnnotationPresent(UrlParameter.class)) {
        		 anno = f.getAnnotation(UrlParameter.class);
        		 String map = anno.map();
        		 String format = anno.dateFormat();
        		 String fieldName = f.getName();
        		
             	 Object fieldValue = PropertyUtils.getProperty(obj, fieldName);
             	 if(null == fieldValue) {
             		stringResult.append(prefix);
             	 } else {
             		if (!"".equals(map)) {
                        String[] strArr = map.split(",");
                        Map<String, String> strMap = new HashMap<>();
                        for (String str : strArr) {
                            strMap.put(str.substring(0, str.indexOf('=')), str.substring(str.indexOf('=') + 1));
                        }
                        if (first) {
                       	 stringResult.append(strMap.get(fieldValue));
                        } else {
                       	 stringResult.append(prefix).append(strMap.get(fieldValue));
                        }
                        
                    } else if (!"".equals(format)) {
                        if ((fieldValue instanceof Date)) {
                            Date date = (Date) fieldValue;
                            String dateStr = dateToString(date, format);
                            if (first) {
                           	 stringResult.append(dateStr);
                            } else {
                           	 stringResult.append(prefix).append(dateStr);
                            }
                        }
                    } else {
                   	 String parameterValue = "";
                   	 if (null != fieldValue) {
                   		 parameterValue = fieldValue.toString();
                   	 } else {
                   		 parameterValue = "";
                   	 }
                   	 if (first) {
                   		 stringResult.append(parameterValue);
                   	 } else {
                   		 stringResult.append(prefix).append(parameterValue);
                   	 }
                   	 
                    }
             	 }
                 
                 first = false;
        	 }
        	
        }
		
		return stringResult.toString();
	}
	
	
	private static <T> boolean executeCondition(Object obj, Class<T> clazz,String condition) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(StringUtils.isBlank(condition)){
			return true;
		}
        String fieldName = condition.substring(0, condition.indexOf('=')).trim();
        String values = condition.substring(condition.indexOf('=') + 1).trim();
        String[] valueArr = values.split(",");
        Object fieldValue = PropertyUtils.getProperty(obj, fieldName);
        for(String str:valueArr) {
        	if(str.equals(fieldValue)){
        		return true;
        	}
        		
        }
        return false;
	}


	public static String assembleParams(Map<String, String> params) {
		StringBuffer sb = new StringBuffer();
		int count = 1;
		sb.append("?");
		for(String key :params.keySet()){
			sb.append(key).append("=").append(params.get(key));
			if(count != params.size()){
				sb.append("&");
			}
			count++;
		}
		return sb.toString();
	}
	
	public static String assembleParamsByDivide(Map<String, String> params) {
		StringBuffer sb = new StringBuffer();
		int count = 1;
		for(String key :params.keySet()){
			sb.append(key).append("/").append(params.get(key));
			if(count != params.size()){
				sb.append("&");
			}
			count++;
		}
		return sb.toString();
	}
}
