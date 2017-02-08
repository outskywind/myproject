package jvm.JDKProxy;

public class UserImpl implements UserInterface{

	public String speak() {
		System.out.println("...target method invoke...");
		return "ok";
	}

}
