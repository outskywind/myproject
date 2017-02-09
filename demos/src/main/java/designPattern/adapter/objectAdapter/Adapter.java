package designPattern.adapter.objectAdapter;

import java.util.HashMap;

public class Adapter extends RequiredClass{
	
	//重写该功能还是。组合该功能看情况。
	Server server = new Server();
	
	public HashMap<String,Object> invoke(){
		//
		HashMap<String,Object> result = super.invoke();
		
		//
		String code = server.getResult();
		
		result.put("code", code);
		
		return result;
		
	}
	

}
