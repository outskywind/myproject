package format;

import org.junit.Test;

public class Longtt {
	
	@Test
	public void testCount() {
	
	 String emailRegx = "^[\\w\\-\\.]+@[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)?(\\.[a-zA-Z0-9]+(\\-[a-zA-Z0-9]+)?)*\\.[a-zA-Z]{2,4}$";
	
	 System.out.println(emailRegx.matches("@12gg3.com"));
	 
	 
	}
	
	
	//Long  String 不可修改
	public static void countLong(String count) {
		long countlong = Long.valueOf(count);
		
	}
	public void test(){
		float f =1.0f;
		double d = 1.0;
		int l ="123".length();
		char c = 17;
		
		
		
	}

}
