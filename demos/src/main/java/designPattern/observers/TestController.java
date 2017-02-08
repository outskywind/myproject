package designPattern.observers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Observer;
import org.junit.Test;

public class TestController {
	
	String str;
	@Test
	public void testObserver() {
		
		Observed subject = new Observed();
		
		Observer obs = new ObserverImpl();
		
		subject.addObserver(obs);
		
		//change value
		
		subject.setValue(0);
		
		Iterator it = Arrays.asList(new String[]{"01","02"}).iterator();
	}
	
	

}
