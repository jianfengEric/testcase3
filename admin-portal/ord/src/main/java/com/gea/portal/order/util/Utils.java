package com.gea.portal.order.util;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

public class Utils {

	public static String genUUIDWOHypen() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String encryptThenBase64Encode(String source) throws Exception {
		byte[] encryptedBytes = AES.encryptByDefaultStrKey(source);
		return Base64.encodeBase64URLSafeString(encryptedBytes);
	}

	public static String Base64DecodeThenAESDecrypt(String source) throws Exception {
		byte[] encryptedBytes = Base64.decodeBase64(source);
		return AES.decryptByDefaultStrKey(encryptedBytes);
	}
}
