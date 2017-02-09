package designPattern.abstractFactory;

public class Visualization {
	
	private AbstractUIFactory abf;
	
	public Visualization(AbstractUIFactory abf){
		
		this.abf=abf;
	}
	
	public Object createButton(){
		return this.abf.createButton();
	}

}
