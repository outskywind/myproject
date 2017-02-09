package designPattern.adapter.objectAdapter;

import java.util.Map;

public class Client {
	
	//客户端类 ( Spring 的 dispatcherservlet )  调用了一个Required类，
	private RequiredClass requiredClass;
	
	public Client(){
		this.requiredClass = new Adapter();
	}
	
	public void dosth(){
		Map result = this.requiredClass.invoke();
		
		System.out.println(result.get("code"));
	}
	

}
