package designPattern.singleton;

import org.junit.Test;

public class TestCase {
	
	@Test
	public void test() {
		
		Factory f = Factory.getInstance();
		f.record();
		
		
		Factory f2 = Factory.getInstance();
		f2.record();
		
	}

}
