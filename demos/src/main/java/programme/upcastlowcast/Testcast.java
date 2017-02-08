package programme.upcastlowcast;

import org.junit.Test;

public class Testcast {
	
	
	private void handle(Object handler) {
		
		String name = (String)((UpInterface)handler).getName();
		System.out.println(name);
	}
	
	
	@Test
	public void Testxx() {
		InstanceClass handler  = new InstanceClass();
		
		Instance1 handler1 = new Instance1();
		
		handle(handler);
		
		handle(handler1);
		
	}

}
