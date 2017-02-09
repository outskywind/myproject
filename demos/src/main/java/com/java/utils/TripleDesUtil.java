package com.java.utils;

import java.awt.SystemTray;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.security.provider.SecureRandom;

import com.sun.xml.internal.ws.util.StringUtils;

public  class TripleDesUtil {

	private static Cipher cipher;
	
	/*明文存储*/
	private static byte[] deskey;
	
	private static final String UNICODE_FORM="utf-8";
	
	private TripleDesUtil() {
		try {
			if (cipher == null) {
				cipher = Cipher.getInstance("DESede");
			}
			//deskey = Configuration.getValue("pa18.tripleDes.key");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	public static String encrypt(String value, String desKey) {
		byte[] bk = new byte[24];
		if(desKey==null){
			return null;
		} else if(desKey.length()<24){
			int hash =  desKey.hashCode() & 0xFF;
			for(int i=0;i<bk.length;i++){
				//bk[i] =(desKey.hashCode() & 0xFF);
			}
			
		}
		String result = null;
		try {
			
			SecretKeySpec key = new SecretKeySpec(desKey.getBytes(), 0, 24, "DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] str = cipher.doFinal(value.getBytes(UNICODE_FORM));
			System.out.println("加密后的byte[]长度："+str.length);
			result = byte2hex(str);
		    System.out.println("转换String后的byte[]长度："+result.getBytes().length);

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return result;
		
	}
	
	public static String decrypt(String value, String desKey) {
		
		String result = null;
		
		try {
			byte[] str = hex2byte(value);
			System.out.println("转换后的密文byte[]长度："+str.length);
			SecretKeySpec key = new SecretKeySpec(desKey.getBytes(), 0, 24, "DESede");
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = new String(cipher.doFinal(str),UNICODE_FORM);

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return result;
		
	}
	
	// 将加密后的密文转换成16进制字符串
		private static String byte2hex(byte[] b)
		{
			String hs = "";
			String stmp = "";
			for (int n = 0; n < b.length; n++)
			{
				stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
				if (stmp.length() == 1)
					hs = hs + "0" + stmp;
				else
					hs = hs + stmp;
				//if (n < b.length - 1)
					//hs = hs + ":";
			}
			return hs.toUpperCase();
		}
	
		// 将16进制密文字符串反转换为byte[]
		private static byte[] hex2byte(String word) throws StringIndexOutOfBoundsException
		{
			byte[] b = word.getBytes();
			if ((b.length % 2) != 0)
			{
				throw new StringIndexOutOfBoundsException();
			}
			byte[] b2 = new byte[b.length / 2];

			for (int n = 0; n < b.length; n += 2)
			{
				String item = new String(b, n, 2);
				b2[n / 2] = (byte) Integer.parseInt(item, 16);
			}

			return b2;
		}
		
	public static void main(String[] args) {
		String desKey ="keykeykeykeykeykeykeykeykeykeykeykey";
		TripleDesUtil util = new TripleDesUtil();
		String conent = "fdasfas倒萨uisfads大无畏挖啊……&*%……￥￥￥shhhadshudguasdussdrrerereerweqrgfqweurgqwuerg8q3u4triwuqergftuiasefgiasdufgasdf";
		System.out.println("原文："+conent);
        String descontentString = util.encrypt(conent, desKey);
        System.out.println("密文："+descontentString);
        String content2 = util.decrypt(descontentString, desKey);
        System.out.println("解密后的明文："+content2);
	}

}
