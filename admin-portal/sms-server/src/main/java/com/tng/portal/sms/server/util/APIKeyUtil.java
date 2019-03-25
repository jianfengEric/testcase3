package com.tng.portal.sms.server.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.util.PropertiesUtil;


/**
 * Created by Zero on 2017/1/20.
 */
public class APIKeyUtil {
    private static  final Logger logger = LoggerFactory.getLogger(APIKeyUtil.class);

    private static final HashFunction SHA256 = Hashing.sha256();
    private static final String CHARSET = "UTF-8";
    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static String[] randomKeys = { "AAIR","AKJH","ORMQ","MQOQ","91JI","1PA0","%1c5",")AJR" };
    private static String encryptSeparator = "|";
    private static String decryptSeparator = "\\|";
    private  SimpleDateFormat sdfYyyymmddHhmmss = new SimpleDateFormat(DateCode.dateFormatSs);
    private static final String API_KEY_MODULE=PropertiesUtil.getAppValueByKey("api.key.module");
    private static final String API_KEY_IV=PropertiesUtil.getAppValueByKey("api.key.iv");
    private static final String API_KEY_ENCRYPTION=PropertiesUtil.getAppValueByKey("api.key.encryption");

    private String encryptionKey;
    private byte[] iv;

    public APIKeyUtil(String encryptionKey, byte[] iv) {
        this.encryptionKey = encryptionKey;
        this.iv = iv;
    }

    public static String getKey(){
        APIKeyUtil util = new APIKeyUtil(
                API_KEY_ENCRYPTION,
                StringUtil.hexStringToByte(API_KEY_IV));
        return util.getAPIKey(API_KEY_MODULE);
    }

    public APIKey validation(List<Module> modules, String apiKey, Date currentDate, int apiKeyAcceptableMins) {
        APIKey result = new APIKey();
        result.setKey(apiKey);

        byte[] base64DecodedByteArray = {};
        String sourceString = "";
        String randomKey = "";
        String moduleCode = "";
        String generateTime = "";
        String[] splitedStrs = {};

        /** 0. Check Encryption Key And IV first **/
        if (null == encryptionKey ||"".equals(encryptionKey.trim()) || iv == null || iv.length == 0) {
            logger.info("[APIKey] Please provide encryption key and iv !!!");
            return result;
        }

        /** 1. Base64 decode the Base64 String (APIKey) **/
        try {

        	base64DecodedByteArray = Base64.getDecoder().decode(apiKey);
        } catch (Exception e) {
            logger.error("[APIKey] base64 decode error...", e);
            return result;
        }

        /** 2. AES decrypt the encrypted byte[] **/
        try {
            sourceString = decryptByStrKey(base64DecodedByteArray);
        } catch (Exception e) {
            logger.error("[APIKey] Error when AES decrypt the API Key", e);
            return result;
        }

        /** 3. Splite the sourceString by the decrypt seperator \\| **/
        splitedStrs = sourceString.split(decryptSeparator);
        if (!isSplitedStringValid(splitedStrs)) {
            return result;
        }

        /** 4. Check the random key **/
        randomKey = splitedStrs[0];
        if (!isRandomKeyValid(randomKey)) {
            return result;
        }

        /** 5. Check module code **/
        moduleCode = splitedStrs[1];
        if (!isModuleCodeValid(moduleCode, modules)) {
            return result;
        }

        /** 6. Check APIKey generate time **/
        generateTime = splitedStrs[2];
        if (!isGenerateTimeValid(generateTime, currentDate, apiKeyAcceptableMins)) {
            return result;
        }

        /** 7. If All above checking are passed, APIKey = valid **/
        result.setModuleCode(moduleCode);
        result.setGenerateTime(generateTime);
        result.setValid(true);
        return result;
    }

    public String getAPIKey(String module) {
        if (null == encryptionKey ||"".equals(encryptionKey.trim()) || iv == null || iv.length == 0) {
            logger.info("[APIKey] Please provide encryption key and iv !!!");
            return "";
        }

        String todayStr = sdfYyyymmddHhmmss.format(new Date());
        String randomKey = getRandomKey();
        String appendedString = getAppendedString(module, randomKey, todayStr);
        byte[] excryptedByte = null;
        try {
            excryptedByte = encryptByStrKey(appendedString);
        } catch (Exception e) {
            logger.error("[APIKey] Unable to AES encrypt the appendedString", e);
            return "";
        }

        return BaseEncoding.base64().encode(excryptedByte);
    }





    private boolean isSplitedStringValid(String[] splitedStrs) {
        if (splitedStrs == null || splitedStrs.length != 3) {
            logger.info("[APIKey] Invalid content...cannot splite into 3 content");
            return false;
        }

        return true;
    }

    private boolean isRandomKeyValid(String randomKey) {
        for (String key : randomKeys) {
            if (key.equals(randomKey)) {
                return true;
            }
        }

        logger.info("[APIKey] Invalid random key");
        return false;
    }

    private boolean isModuleCodeValid(String moduleCode, List<Module> modules) {
        for (Module m : modules) {
            if (m.getCode().equals(moduleCode)) {
                return true;
            }
        }

        logger.info("[APIKey] Invalid module code | moduleCode = "+moduleCode);
        return false;
    }

    private boolean isGenerateTimeValid(String generateTime, Date currentDate, int apiKeyAcceptableMins) {
        try {
            Date generateDateTime = sdfYyyymmddHhmmss.parse(generateTime);
            Calendar c = Calendar.getInstance();
            c.setTime(generateDateTime);
            c.add(Calendar.MINUTE, apiKeyAcceptableMins);

            if (c.getTime().compareTo(currentDate) > -1) {
                return true;
            } else {
                logger.info("[APIKey] Invalid generate time, probably expired");
                return false;
            }
        } catch (Exception e) {
            logger.error("[APIKey] Invalid generate time", e);
        }

        return true;
    }

    private String getAppendedString(String module, String randomKey, String todayStr) {
        StringBuilder sb = new StringBuilder();
        sb
                .append(randomKey)
                .append(encryptSeparator)
                .append(module)
                .append(encryptSeparator)
                .append(todayStr);

        return sb.toString();
    }

    private static String getRandomKey() {
        Random ran = new Random();
        return randomKeys[ran.nextInt(randomKeys.length)];
    }

    private byte[] encryptByStrKey(String plainText) throws Exception
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(encryptionKey.getBytes("UTF-8"));

        return encrypt(plainText.getBytes(CHARSET), iv, hash);
    }

    private String decryptByStrKey(byte[] cipherText) throws Exception
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(encryptionKey.getBytes("UTF-8"));
        return new String(decrypt(cipherText, iv, hash), CHARSET);
    }

    private byte[] encrypt(byte[] plainText, byte[] iv, byte[] encryptionKey) throws Exception
    {
        Cipher cipher = Cipher.getInstance(ALGO);
        HashCode hashedKey = SHA256.hashBytes(encryptionKey);
        SecretKeySpec key = new SecretKeySpec(hashedKey.asBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(validateIV(iv)));
        return cipher.doFinal(plainText);
    }


    private byte[] decrypt(byte[] cipherText, byte[] iv, byte[] encryptionKey) throws Exception
    {
        Cipher cipher = Cipher.getInstance(ALGO);
        HashCode hashedKey = SHA256.hashBytes(encryptionKey);
        SecretKeySpec key = new SecretKeySpec(hashedKey.asBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(validateIV(iv)));
        return cipher.doFinal(cipherText);
    }

    private byte[] validateIV(byte[] iv)
    {
        if (iv.length == 16) {
            return iv;
        } else {
            return Arrays.copyOf(iv, 16);
        }
    }




}
