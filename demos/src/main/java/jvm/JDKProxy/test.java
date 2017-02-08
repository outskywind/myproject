package jvm.JDKProxy;

import org.junit.Test;

public class test {
	
	@Test
	public void test() {
		MyProxyFactory tMyProxy = new MyProxyFactory((UserInterface)new UserImpl());
		MyProxyFactory.test();
		//代理类实例
		UserInterface tUser = (UserInterface)tMyProxy.getObject();
		tUser.speak();
		
	}

}
