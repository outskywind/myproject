package concurrency.threadStatus;

public class StatusTest {
	
	private final String key;
	
	//要么直接指定初始化值，要么在“所有”的构造函数中指定
	public StatusTest(){
		key="";
	}
	
	
	
	
	private class Calculator implements Runnable{
		int i;
		
		public Calculator(int calculator){
			this.i=calculator;
		}

		@Override
		public void run() {
			//计算这个值
			for(int i=1;i<=9;i++){
				
				
				
			}
			
			
			
			
			
		}
		
	}

}
