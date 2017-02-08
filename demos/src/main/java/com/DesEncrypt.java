package com; 

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import   java.security.Key;   
import   java.security.SecureRandom;   
import   javax.crypto.Cipher;   
import   javax.crypto.KeyGenerator;   
import   sun.misc.BASE64Decoder;   
import   sun.misc.BASE64Encoder;   
  
/**   
  *   
  *   使用DES加密与解密,可对byte[],String类型进行加密与解寄   
  *   密文可使用String,byte[]存储.   
  *   
  *   方法:   
  *   void   getKey(String   strKey)从strKey的字条生成一个Key   
  *   
  *   String   getEncString(String   strMing)对strMing进行加密,返回String密文   
  *   String   getDesString(String   strMi)对strMin进行解密,返回String明文   
  *   
  *byte[]   getEncCode(byte[]   byteS)byte[]型的加密   
  *byte[]   getDesCode(byte[]   byteD)byte[]型的解密   
  */   
  
public   class   DesEncrypt   {   
	/** 
	02.     * 文件转化为字节数组 
	03.     *  
	04.     * @param file 
	05.     * @return 
	06.     */  
	    public static byte[] getBytesFromFile(File file) {  
	        byte[] ret = null;  
	        try {  
	            if (file == null) {  
	                // log.error("helper:the file is null!");   
	                return null;  
	            }  
	            FileInputStream in = new FileInputStream(file);  
	            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);  
	            byte[] b = new byte[4096];  
	            int n;  
	            while ((n = in.read(b)) != -1) {  
	                out.write(b, 0, n);  
	            }  
	            in.close();  
	            out.close();  
	            ret = out.toByteArray();  
	        } catch (IOException e) {  
	            // log.error("helper:get bytes from file process error!");   
	            e.printStackTrace();  
	        }  
	        return ret;  
	    }  
	    
	    /** 
	    44.     * 字节数组转化为UTF-8 
	    45.     * @param bty 
	    46.     * @return 
	    47.     */  
	        public static String toUTF8(byte[] bty){  
	            try {  
	                if (bty.length > 0) {  
	                    return new String(bty, "UTF-8");  
	                }  
	            } catch (UnsupportedEncodingException e) {  
	                e.printStackTrace();  
	            }  
	            return new String(bty);  
	        }  

	    public static void main(String[] args){
	    	File file= new File("");
	    	String str = toUTF8(getBytesFromFile(file));
	    }

    
}
