package designPattern.observers;

import java.util.Observable;
import java.util.Observer;

public class Observed extends Observable {
	
	private int value=0;
	
	
	public Observed() {
		super();
	}
	
	public Observed(Observer obs) {
		super();
		addObserver(obs);
	}
	
	
	public void setValue(int value) {
		this.value=value;
		setChanged();
		notifyObservers(this.value);
	}

}
