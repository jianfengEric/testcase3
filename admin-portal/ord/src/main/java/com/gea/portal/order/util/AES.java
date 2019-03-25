package com.gea.portal.order.util;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class AES {
	private static final String KEY = "IVUJNY!I@U$^NV!(*@&^$HNV*&!^4viuTYRB817v46&*!24vby*&@evbt8&!@rybv871@";
	private static final byte[] IV = { 0x69, -0x32, 0x00, 0x43, 0x1a, 0x04, 0x7f, -0x5e, 0x23, 0x56, -0x4a, 0x2b, -0x55, 0x1d, 0x5c, 0x11 };
	private static final HashFunction SHA256 = Hashing.sha256();
	private static final String CHARSET = "UTF-8";
	private static final String sha256 = "SHA-256";
	private static final String ALGO = "AES/CBC/PKCS5Padding";

	public static byte[] encrypt(String plainText, byte[] encryptionKey) throws Exception
	{
		return encrypt(plainText, IV, encryptionKey);
	}
	
	public static String decrypt(String cipherText, byte[] encryptionKey) throws Exception
	{
		return decryptToString(cipherText, IV, encryptionKey);
	}
	
	public static byte[] encrypt(byte[] plainText, byte[] encryptionKey) throws Exception
	{
		return encrypt(plainText, IV, encryptionKey);
	}
	
	public static String decrypt(byte[] cipherText, byte[] encryptionKey) throws Exception
	{
		return decryptToString(cipherText, IV, encryptionKey);
	}

	public static byte[] encrypt(String plainText, byte[] iv, byte[] encryptionKey)	throws Exception
	{
		return encrypt(plainText.getBytes(CHARSET), iv, encryptionKey);
	}
	
	public static byte[] decrypt(String cipherText, byte[] iv, byte[] encryptionKey) throws Exception
	{
		return decrypt(cipherText.getBytes(CHARSET), iv, encryptionKey);
	}
	
	public static byte[] encrypt(byte[] plainText, byte[] iv, byte[] encryptionKey)	throws Exception
	{
		Cipher cipher = Cipher.getInstance(ALGO);
		HashCode hashedKey = SHA256.hashBytes(encryptionKey);
		SecretKeySpec key = new SecretKeySpec(hashedKey.asBytes(), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(validateIV(iv)));
		return cipher.doFinal(plainText);
	}

	public static byte[] decrypt(byte[] cipherText, byte[] iv, byte[] encryptionKey) throws Exception
	{
		Cipher cipher = Cipher.getInstance(ALGO);
		HashCode hashedKey = SHA256.hashBytes(encryptionKey);
		SecretKeySpec key = new SecretKeySpec(hashedKey.asBytes(), "AES");
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(validateIV(iv)));
		return cipher.doFinal(cipherText);
	}
	
	public static String decryptToString(String cipherText, byte[] iv, byte[] encryptionKey) throws Exception
	{
		return new String(decrypt(cipherText, iv, encryptionKey), CHARSET);
	}
	
	public static String decryptToString(byte[] cipherText, byte[] iv, byte[] encryptionKey) throws Exception
	{
		return new String(decrypt(cipherText, iv, encryptionKey), CHARSET);
	}
	
	public static byte[] decryptToByteArray(byte[] cipherText, byte[] encryptionKey) throws Exception
	{
		return decrypt(cipherText, IV, encryptionKey);
	}
	
	public static byte[] encryptByStrKey(String plainText, String encryptionKey) throws Exception
	{
		MessageDigest digest = MessageDigest.getInstance(sha256);
		byte[] hash = digest.digest(encryptionKey.getBytes(CHARSET));
		
		return encrypt(plainText.getBytes(CHARSET), IV, hash);
	}
	
	public static String decryptByStrKey(byte[] cipherText, String encryptionKey) throws Exception
	{
		MessageDigest digest = MessageDigest.getInstance(sha256);
		byte[] hash = digest.digest(encryptionKey.getBytes(CHARSET));
		return new String(decrypt(cipherText, IV, hash), CHARSET);
	}
	
	public static byte[] encryptByDefaultStrKey(String plainText) throws Exception
	{
		MessageDigest digest = MessageDigest.getInstance(sha256);
		byte[] hash = digest.digest(KEY.getBytes(CHARSET));
		
		return encrypt(plainText.getBytes(CHARSET), IV, hash);
	}
	
	public static String decryptByDefaultStrKey(byte[] cipherText) throws Exception
	{
		MessageDigest digest = MessageDigest.getInstance(sha256);
		byte[] hash = digest.digest(KEY.getBytes(CHARSET));
		return new String(decrypt(cipherText, IV, hash), CHARSET);
	}
	
	private static byte[] validateIV(byte[] iv)
	{
		if(iv != null)
		{
			if(iv.length == 16)
			{
				return iv;
			}else
			{
				return Arrays.copyOf(iv, 16);
			}
		}
		return IV;
	}
}