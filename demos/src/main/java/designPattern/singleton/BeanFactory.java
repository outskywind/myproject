package designPattern.singleton;

import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
	
	private static ConcurrentHashMap<String,Object> singleton = new ConcurrentHashMap<String, Object>(64);
	

}
