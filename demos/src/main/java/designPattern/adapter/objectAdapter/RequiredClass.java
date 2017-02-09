package designPattern.adapter.objectAdapter;

import java.util.HashMap;

public class RequiredClass {
	
	//这个类的功能由client调用,但是客户端需求改变，要增加功能，SPI 里有现成的功能
	//准备重用
	public HashMap<String,Object> invoke(){
		
		
		return new HashMap<String, Object>();
	}

}
