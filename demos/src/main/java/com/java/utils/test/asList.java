package com.java.utils.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class asList {
	private static final String[] DMYWtype= {"01","02","03","04","05","06","07","08","09","10","11"};
	
	@Test
	public void testCatst() {
		List<String> DMYW_type = Arrays.asList(DMYWtype);
	}
	

}
