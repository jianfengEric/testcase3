package com.tng.portal.sms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LocalDateUtils {

    private static Logger logger = LoggerFactory.getLogger(LocalDateUtils.class);

	/** 
     * Get the next date
     *  
     * @param specifiedDay 
     * @return 
     */  
    public static Date getSpecifiedMinuteAfter(String specifiedMinute) {  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yyyyMMddHHmm").parse(specifiedMinute);  
        } catch (ParseException e) {  
            logger.error("specifiedMinute:{}",specifiedMinute,e);
        }  
        c.setTime(date);  
        int minute = c.get(Calendar.MINUTE);  
        c.set(Calendar.MINUTE, minute + 1);  
  
        return c.getTime();  
    }  
}
