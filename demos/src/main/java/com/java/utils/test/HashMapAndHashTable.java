package com.java.utils.test;

import java.util.HashMap;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class HashMapAndHashTable {
	
	private static HashMap  datasHashMap;
	private static Hashtable datasHashtable;
	
	static {
		datasHashMap = new HashMap();
		datasHashtable = new Hashtable();
	}
	
	public static void main(String[] args){
		HashMapAndHashTable datas= new HashMapAndHashTable();
		datas.datasHashMap.remove(null);
		datas.datasHashtable.put("", null);
		
		datas.datasHashtable.remove(null);
	}
}
