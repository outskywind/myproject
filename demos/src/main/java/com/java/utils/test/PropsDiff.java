package com.java.utils.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;


/**
 * @author LIUYAJIAO589
 *
 */
public class PropsDiff {
	private static final String preFix = "D:/Users/quanchengyun970/Desktop/";
	
	private static final String devFileName = "context-pa18shopauto.properties";
	private static final String shopDevFileName = "context-pa18shopstg.properties";
	private static final String stgFileName = "context-pa18shopautostg.properties";
	private static final String pirFileName = "pir.properties";
	private static final String npFileName = "np.properties";
	
	public static void main(String[] args) throws IOException{
		Properties devProps = loadProps(preFix+devFileName);
		Properties stgProps = loadProps(preFix+stgFileName);
			
		
		Map<String,String> diffs = new HashMap<String,String>();
		for(Entry e : stgProps.entrySet()){
			String key = (String) e.getKey();
			String val = (String) e.getValue();
			if(!devProps.containsKey(key)){
				diffs.put(key, val);
				System.out.println(key+"="+val);
			}
		}
		
		if(diffs.isEmpty()) System.out.println("");
		
	}
	
	public static Properties loadProps(String fileName) throws IOException {
		Properties props = new Properties();
		InputStream ins = new FileInputStream(new File(fileName));
		props.load(ins);
		ins.close();
		return props;
	}
}
