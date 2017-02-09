package designPattern.abstractFactory;

import org.junit.Test;

public class Testcase {
	
	@Test
	public void testXX(){
		
		Visualization vi = new Visualization(new UNIXUIFactory());
		
		String obj = (String)vi.createButton();
		
		System.out.println(obj);
		
	}

}
