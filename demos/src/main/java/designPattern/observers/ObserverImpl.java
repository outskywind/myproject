package designPattern.observers;

import java.util.Observable;
import java.util.Observer;

public class ObserverImpl implements Observer{

	public void update(Observable o, Object arg) {
		
		if( o instanceof Observed) {
			System.out.println(" Observed changed "+ arg);
		}
		
		System.out.println("subject value changed");
		
	}

}
