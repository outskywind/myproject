package designPattern.abstractFactory;

public class WinUIFactory implements AbstractUIFactory{

	public String createButton() {
		
		return("winButton");
		
	}

	public String createIcon() {
		
		return("winIcon");
	}

}
