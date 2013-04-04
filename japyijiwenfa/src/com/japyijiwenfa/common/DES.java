package com.japyijiwenfa.common;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*******************************************************
 * Title: DES Algorithm * Author: Hellant * Email: hongz.xu@gmail.com * * Note:
 * This program should be just used for study! * Thank you! *
 ******************************************************/

public class DES {
	public static String encryptDES(String encryptString, String encryptKey) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DESede");
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
		return Base64.encode(encryptedData);
		//return android.util.Base64.encodeToString(encryptedData, android.util.Base64.DEFAULT);
	}
	
	public static byte[] decryptDES(byte[] byteMi, String decryptKey) throws Exception {
		
		IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DESede");
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
	 
		return decryptedData;
	}
}