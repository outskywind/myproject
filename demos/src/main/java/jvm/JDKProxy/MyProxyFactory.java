package jvm.JDKProxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import sun.misc.ProxyGenerator;

public class MyProxyFactory implements InvocationHandler {
	
	/**
	 * 代理类实例
	 */
	private  Object proxy ;
	
	private  Object targetInterface ;

	/**
	 * proxy is the instance that invoked this method
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		System.out.println("...before...");
		
		method.invoke(this.targetInterface, args);
		
		System.out.println("...after...");
		
		return null;
	}
	
	public MyProxyFactory(Object targetInterface) {
		this.targetInterface = targetInterface;
	}
	
	public  Object getObject() {
		this.proxy = Proxy.newProxyInstance(targetInterface.getClass().getClassLoader(), 
				targetInterface.getClass().getInterfaces(), this);
		return this.proxy;
	}
	
	
	
	public static void test() {
		UserInterface targetInterface=new UserImpl();
		String proxyName = "jvm.JDKProxy.UserInterface";
		byte[] proxyClassFile =	ProxyGenerator.generateProxyClass(
			    proxyName, targetInterface.getClass().getInterfaces());
		
		FileOutputStream out =null;
		try {
			out= new FileOutputStream(new File("d:/$Proxy11.class"));
			out.write(proxyClassFile);
			out.flush();
			}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
