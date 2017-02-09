package designPattern.callback;

public class Acaller {
	
	//
	public Object doSth(InterfaceCallback callback) {
		//do sth
		return callback.handle();
		
	}
	
	public void handleRequest() {
		
		//do sth
		String flag = (String)doSth(new InterfaceCallback() {
			
			public Object handle() {
				//do sth
				invoke();
				
				return "hello";
			}
			
		}); 
		
	}
	
	public void invoke() {
		
		//do sth
		System.out.println("hello world");
		
	}
	

}
