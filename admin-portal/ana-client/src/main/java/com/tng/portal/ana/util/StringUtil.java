package com.tng.portal.ana.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Zero on 2016/11/14.
 */
public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
    
    public static boolean isEmpty(String value){
        return value==null || value.equals("");
    }

    // Gets a random string of specified digits ( Contains lowercase letters, capital letters, and digits. ,0<length)
    public static String getRandomString(int length) {
        // Random character library with random strings 
        String keyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        int len = keyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(keyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return sb.toString();
    }
}
