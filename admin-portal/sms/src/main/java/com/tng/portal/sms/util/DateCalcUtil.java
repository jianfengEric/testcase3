package com.tng.portal.sms.util;

import com.tng.portal.common.constant.DateCode;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateCalcUtil {
	public static Date removeTimePart(Date date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatDd);
		return dateFormat.parse(dateFormat.format(date));
	}

	public static Date parseDateTime(String inputDate) throws Exception {
		DateFormat timeFormat = new SimpleDateFormat(DateCode.dateFormatSs);
		return timeFormat.parse(inputDate);
	}

	public static Date parseDate(String inputDate) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatDd);
		return dateFormat.parse(inputDate);
	}

	public static String formatDate(Date date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatDd);
		return dateFormat.format(date);
	}

	public static String formatDate(Date date, DateFormat format) {

		if (format == null)
			return null;
		return format.format(date);
	}

	public static String formatDatetime(Date date) {
		DateFormat timeFormat = new SimpleDateFormat(DateCode.dateFormatSs);
		return formatDate(date, timeFormat);

	}

	public static Timestamp parseTimestamp(String inputDate) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(inputDate));
		return new Timestamp(calendar.getTimeInMillis());
	}

	public static String getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
	}

	public static Integer getHourOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static String getHourAndMinuteOfDay(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(date);
	}

	public static Date getNearDay(Date date, int offset) {
		if (offset == 0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int target = offset;
		int step = 1;
		if (target < 0) {
			target *= -1;
			step = -1;
		}
		while (target > 0) {
			calendar.add(Calendar.DAY_OF_YEAR, step);
			target--;
		}
		return calendar.getTime();
	}

	public static Date getNearDateTime(Date date, int offset, int type) {
		if (offset == 0)
			return date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int target = offset;
		int step = 1;
		if (target < 0) {
			target *= -1;
			step = -1;
		}
		while (target > 0) {
			calendar.add(type, step);
			target--;
		}
		return calendar.getTime();
	}

	public static int getDateDifferenceByType(Date date1, Date date2, int calendarType) {
		Date earlier;
		Date later;
		Boolean switched = false;
		Calendar calendarEarlier = Calendar.getInstance();
		Calendar calendarLater = Calendar.getInstance();
		if (date1.equals(date2)) {
			return 0;
		}
		if (date1.after(date2)) {
			earlier = date2;
			later = date1;
			switched = true;
		} else {
			earlier = date1;
			later = date2;
			switched = false;
		}
		calendarEarlier.setTime(earlier);
		calendarLater.setTime(later);
		int count = 0;
		while (calendarEarlier.before(calendarLater)) {
			calendarEarlier.add(calendarType, 1);
			count++;
		}
		if (switched) {
			count *= -1;
		}
		return count;
	}

	public static Date getNextHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		return calendar.getTime();
	}

	public static Date getEndTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 59);
		calendar.add(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getDatePart(Date date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatDd);
		String dateStr = dateFormat.format(date);
		return dateFormat.parse(dateStr);
	}

	public static Date getBeginDateTime(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date getEndDateTime(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getNextHalfHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 30);
		return calendar.getTime();
	}

	public static String getLastHalfHourStr(String dateTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(DateCode.dateFormatMm);
		Date date = format.parse(dateTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, -30);
		return format.format(calendar.getTime());
	}

	public static String getNextHalfHourStr(String dateTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(DateCode.dateFormatMm);
		Date date = format.parse(dateTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 30);
		return format.format(calendar.getTime());
	}
	/***
	 * get currentDate firstDate and last Date
	 * @param currentDate
	 * @return list<date>
	 */
	public static List<Date> getDates(Date currentDate) {
		List<Date> dates=new ArrayList<>();
		if(null!=currentDate){
			Calendar frist = Calendar.getInstance();
			frist.setTime(currentDate);
			frist.add(Calendar.MONTH, 0);
			frist.set(Calendar.DAY_OF_MONTH, 1);
			dates.add(frist.getTime());
			Calendar last = Calendar.getInstance();
			last.setTime(currentDate);
			last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(Calendar.DAY_OF_MONTH));
			dates.add(last.getTime());
		}
		return dates;
	}
	
	/***
	 * get list<date> between  startDate and  endDate  
	 * @return list<date>
	 */
	public static List<Date> getDates(Date startDate,Date endDate) {
		List<Date> dates=new ArrayList<>();
		if(null!=startDate&&null!=endDate){
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			Calendar ca = Calendar.getInstance();
			ca.setTime(endDate);
			while (true) {
				if (c.getTimeInMillis() <= ca.getTimeInMillis()) {
					dates.add(c.getTime());
				} else {
					break;
				}
				c.add(Calendar.DATE, 1);
			}
		}
		
		return dates;
	}

	public static String previousMonthFirstDay(){
			SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatDd);
	       String firstDay = "";
	        // Get the first day of the month before. 
	        Calendar   cal1=Calendar.getInstance();// Get the current date
	        cal1.add(Calendar.MONTH, -1);
	        cal1.set(Calendar.DAY_OF_MONTH,1);// Set to 1 Number , The current date is the first day of the month.
	        firstDay = dateFormat.format(cal1.getTime());
	        return firstDay;
	}
	
	public static String previousMonthLastDay(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatDd);
		  String lastDay = "";
	        // Get the last day of the month before. 
	        Calendar cale = Calendar.getInstance();  
	        cale.set(Calendar.DAY_OF_MONTH,0);// Set to 1 Number , The current date is the first day of the month. 
	        lastDay = dateFormat.format(cale.getTime());
	        return lastDay;
	}
	
	public static String nextMonthFirstDay(String dateString) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatDd);
		Date date = dateFormat.parse(dateString);
       String firstDay = "";
        // Get the first day of the month before. 
        Calendar   cal1=Calendar.getInstance();// Get the current date
        cal1.setTime(date);
        cal1.add(Calendar.MONTH, 1);
        cal1.set(Calendar.DAY_OF_MONTH,1);// Set to 1 Number , The current date is the first day of the month.
        firstDay = dateFormat.format(cal1.getTime());
        return firstDay;
	}

	public static String nextMonthLastDay(String dateString) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatDd);
		Date date = dateFormat.parse(dateString);
		  String lastDay = "";
	        // Get the last day of the month before. 
	        Calendar cale = Calendar.getInstance();  
	        cale.setTime(date);
	        cale.add(Calendar.MONTH, 2);
	        cale.set(Calendar.DAY_OF_MONTH,0);
	        lastDay = dateFormat.format(cale.getTime());
	        return lastDay;
	}

	/**
	 *  Get today's start time 
	 * @return
	 */
	public static final Date getTimeStartToday(){
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    return calendar.getTime();
	}
	
	/**
	 *  Get the end of today. 
	 * @return
	 */
	public static final Date getTodayEndingTime(){
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.add(Calendar.DAY_OF_MONTH, 1);
	    calendar.add(Calendar.SECOND, -1);
	    return calendar.getTime();
	}
	
	/** 
     *  Got a few days ago.  
     *  
     * @param d 
     * @param day 
     * @return 
     */  
    public static final String getDateBefore(Date d, int day) {  
    	DateFormat timeFormat = new SimpleDateFormat(DateCode.dateFormatSs);
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return timeFormat.format(now.getTime());  
    }    
    /** 
     *  Get a few days later. Get a few days later.  
     *  
     * @param d 
     * @param day 
     * @return 
     */  
    public static final String getDateAfter(Date d, int day) {  
    	DateFormat timeFormat = new SimpleDateFormat(DateCode.dateFormatSs);
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
        return timeFormat.format(now.getTime());  
        
    }
	/***
	 * get current date past  xx month  time
	 * @param d
	 * @return
	 */
    public static final Date getDateBeforeMonth(Date d) {
    	Calendar ca = Calendar.getInstance();//  Get one Calendar An example   
		ca.setTime(d); //  Set the time to the current time.   
		ca.add(Calendar.MONTH, -6);//  Month reduction 6 
		return ca.getTime();
    }

	public static String getMonthStartDate(String yyyyMM) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(DateCode.dateFormatMM);
		SimpleDateFormat format = new SimpleDateFormat(DateCode.dateFormatDd);
		Date date = sdf.parse(yyyyMM);
		// Instance Calendar  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);
		// set Calendar to this Month's first day
		calendar.set(Calendar.DATE, 1);  
		return format.format(calendar.getTime());
	}
	public static String getMonthEndDate(String yyyyMM) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(DateCode.dateFormatMM);
		SimpleDateFormat format = new SimpleDateFormat(DateCode.dateFormatDd);
		Date date = sdf.parse(yyyyMM);
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);
		// set Calendar to next Month's  
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);  
		// set Calendar to next Month's first day
		calendar.set(Calendar.DATE, 1);  
		calendar.add(Calendar.DATE, -1);  
		return format.format(calendar.getTime());
	}
	
	public static String formatDateNoDivide(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateCode.dateFormatMd);
		return dateFormat.format(date);
	}
	
	
}
