package com.tng.portal.common.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class CoderUtil {
	private static final Logger logger = LoggerFactory.getLogger(CoderUtil.class);
	
	static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";
	static boolean isInited = false;

	private static byte[] encrypt(String content, String password) {
		try {
			byte[] keyStr = getKey(password);
			SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
			Cipher cipher = Cipher.getInstance(ALGORITHM_STR);

			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(1, key);

			return cipher.doFinal(byteContent);
		} catch (Exception e) {
			logger.error("error", e);
		} 
		return new byte[]{};
	}

	private static byte[] decrypt(byte[] content, String password) {
		try {
			byte[] keyStr = getKey(password);
			SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
			Cipher cipher = Cipher.getInstance(ALGORITHM_STR);

			cipher.init(2, key);
			return cipher.doFinal(content);
		} catch (Exception e) {
			logger.error("error", e);
		} 
		return new byte[]{};
	}

	private static byte[] getKey(String password) {
		byte[] rByte = null;
		if (password != null) {
			rByte = password.getBytes();
		} else {
			rByte = new byte[24];
		}
		return rByte;
	}

	private static String parseByte2HexStr(byte[] buf) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return new byte[]{};
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = ((byte) (high * 16 + low));
		}
		return result;
	}

	public static String encrypt(String content) {
		if(StringUtils.isBlank(content)){
			return content;
		}
		return parseByte2HexStr(encrypt(content, "abcdefgabcdefg12")).toLowerCase();
	}

	public static String decrypt(String content) {
		if(StringUtils.isBlank(content)){
			return content;
		}
		byte[] b = decrypt(parseHexStr2Byte(content.toUpperCase()), "abcdefgabcdefg12");
		String decodeStr = null;
		try {
			decodeStr = new String(b, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("error", e);
		}
		return decodeStr;
	}
}
