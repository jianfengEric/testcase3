package com.tng.portal.ana.util;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Zero on 2016/11/14.
 */
public class ToolUtil {

    @Value("${default.ip}")
    private static String defaultIP;

    private static final String UNKNOWN="unknown";
    private static final String CSV="csv";

    private static final Logger logger = LoggerFactory.getLogger(ToolUtil.class);
    public static long parseLong(Object obj){
        try {
            return Long.parseLong(obj.toString());
        }catch (Exception e){
            logger.error("Exception",e);
            return -1;
        }
    }
    public static boolean parseBoolean(Object obj){
        try {
            return Boolean.parseBoolean(obj.toString());
        }catch (Exception e){
            logger.error("Exception",e);
            return false;
        }
    }

    public static String getRemoteHost(HttpServletRequest request){
        if(request == null){
            return null;
        }
        String ip = request.getHeader("remote-ip");
        if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)){
            ip = request.getHeader("x-forwarded-for");
        }
        if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?defaultIP:ip;//sonar modify
    }
	/**
		* @Description : 
		* @param name
		* @return  
	*/
	public static boolean isImage(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i == -1) {
		    return false;
		}
		extension = fileName.substring(i + 1);
		String imgArray [] = { "bmp", "dib", "gif", "jfif", "jpe", "jpeg", 
				"jpg", "png" ,"tif", "tiff", "ico" }; 
		List<String> imgList = Arrays.asList(imgArray);
		return imgList.contains(extension.toLowerCase());
	}
	
	public static boolean isCsvFile(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i == -1) {
		    return false;
		}
		extension = fileName.substring(i + 1);
		return CSV.equals(extension.toLowerCase());
	}
}
