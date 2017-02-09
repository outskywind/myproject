package com.java.utils.searchFile;

public class testfilterBlank {
	
	public static void main(String[] args) {
		
		String src = "* ";
		int length = src.length();
		for(int i=0;i<length;i++) {
			if(!Character.isWhitespace(src.charAt(length-i-1))) {
				src=src.substring(0,length-i);
				break;
			}
		}
		System.out.println(src.length());
	}
	

}
