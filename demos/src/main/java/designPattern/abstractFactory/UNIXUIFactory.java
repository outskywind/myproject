package designPattern.abstractFactory;

public class UNIXUIFactory implements  AbstractUIFactory{

	public String createButton() {
		
		return "UNIXButton";
	}

	public String createIcon() {
		
		return "UNIXIcon";
	}

}
