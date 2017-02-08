package programme;

import org.junit.Test;


public class RuntimeExceptionTest {
	
	//自定义的运行时异常
	class NotFoundException extends RuntimeException{
		
		public  NotFoundException(String msg){
			super(msg);
		}
	}
	
	//自定义的检查异常
	class ChekedException extends Exception{
		
		public ChekedException(String msg){
			super(msg);
		}
	}
	
	
	//this method maybe throws exception
	//so it's not required to throws Exception in method definition
	//如果你不想让程序捕获这个异常，就不在方法声明抛出
	public void method1(boolean state) {
		
		if(!state){
			throw new NotFoundException("state error being false");
		}
		
	}
	
	//如果你认为这个异常可以由程序员处理，那么方法声明抛出
	//运行时异常可以不用捕获
	public void method3() throws NotFoundException{
		
		throw new NotFoundException("do nothing just for test");
	}
	
	//检查的异常
	//必须由程序员处理
	//a. 方法向上抛出
	//b.方法自己捕获处理
	public void checkd_method() throws ChekedException{
		
		throw new ChekedException("checked exception ");
	}
	
	
	
	
	//
	public void method2(){
		
		this.method1(false);
		
		//运行时异常可以不用捕获=-=
		this.method3();
		
	}
	
	@Test
	public void test(){
		RuntimeExceptionTest t = new RuntimeExceptionTest();
		t.method2();
		
		
		//
		CallbyReferrence call = new CallbyReferrence();
	}
	
	

}
