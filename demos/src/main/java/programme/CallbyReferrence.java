package programme;

import org.junit.Test;

public class CallbyReferrence {
	
	private int count=0;
	
	static boolean status;
	
	public void call(String args){
		
		args="new";
		
	}
	
	
	@Test
	public void test(){
		String str = "old";
		call(str);
		System.out.println(str);
	}
	
	
	public static void main(String[] args){
		CallbyReferrence call = new CallbyReferrence();
		
		//只要是在这个类里面，都可以直接访问
		//private
		System.out.println(call.count);
		
		//事实证明main()方法与普通方法的特性一样
		System.out.println(status);
		
	}

}
