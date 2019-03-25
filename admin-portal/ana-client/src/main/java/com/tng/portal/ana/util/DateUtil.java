package com.tng.portal.ana.util;

import com.tng.portal.common.constant.DateCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Zero on 2016/11/29.
 */
public class DateUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static Date addDate(Date date,int field,int amount){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field,amount);
        return c.getTime();
    }
    
    public static Date parseDate(String str){
    	SimpleDateFormat sdf = new SimpleDateFormat(DateCode.dateFormatMm);
    	Date date = null;
    	try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			logger.error("str:{}",str,e);
		}
    	return date;
    }
    
    public static String formatDate(Date date){
    	SimpleDateFormat sdf = new SimpleDateFormat(DateCode.dateFormatMm);
    	return sdf.format(date);
    }

	public static Date parseDate(String formatter, String str){
		if(StringUtils.isBlank(formatter) || StringUtils.isBlank(str)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			logger.error("str:{}",str,e);
		}
		return date;
	}
}
