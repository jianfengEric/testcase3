package com.gea.portal.eny.page;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Roger on 2017/9/15.
 */
public class DateUtils {

	public DateUtils() {
		throw new IllegalStateException("DateUtils class");
	}

	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static Date parseDate(String str){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = null;
    	try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			logger.error("str:{}",str,e);
		}
    	return date;
    }
    
    public static String formatDate(Date date){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(date);
    }

	public static String formatDate(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static Date parseDate(String str,String pattern){
		if(StringUtils.isBlank(str)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			logger.error("str:{}",str,e);
		}
		return date;
	}
}
