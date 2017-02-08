package designPattern.callback;

import org.junit.Test;

public class Testcase {
	
	@Test
	public void testMethod() {
		Acaller acaller = new Acaller();
		
		
		
		//do sth
		
		String obj= (String)acaller.doSth(new InterfaceCallback() {
			
			public Object handle() {
				
				//不能使用非final 方法
				//acaller.invoke();
				
				return "method from callback Interface";
			}
		});
		
		System.out.println(obj);
		
	}


}
