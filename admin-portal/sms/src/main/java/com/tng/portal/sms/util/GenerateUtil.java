package com.tng.portal.sms.util;

import java.util.Date;

import org.apache.commons.httpclient.util.DateUtil;

public class GenerateUtil {
	
    public static String generateJobId(String maxJobId){
    	String yearStr = DateUtil.formatDate(new Date(), "yy");
    	if(maxJobId == null || !yearStr.equals(maxJobId.split("-")[0]))
    		return yearStr + "-0001";
    	return yearStr + "-" + getFixStr(Integer.parseInt(maxJobId.split("-")[1]), 4);
    }
	
	public static String getFixStr(int index, int length){
		String str = String.valueOf(++index);
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < length - str.length(); i++) {
			sb.append("0");
		}
		sb.append(str);
		return sb.toString();
	}
}
