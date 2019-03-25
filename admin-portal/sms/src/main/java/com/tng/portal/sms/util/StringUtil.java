package com.tng.portal.sms.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Zero on 2016/11/14.
 */
public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
    public static boolean isEmpty(String value){
        if (value==null || value.equals("")){
            return true;
        }
        return false;
    }
    // Generating random numbers and letters ,
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        // parameter length Represents the number of random numbers generated. 
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // Output letters or numbers? 
            if( "char".equalsIgnoreCase(charOrNum) ) {
                // Is the output capitalized or lowercase? 
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1])& 0xff);
        }
        return result;
    }
    
    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
