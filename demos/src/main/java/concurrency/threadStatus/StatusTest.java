package concurrency.threadStatus;

public class StatusTest {
	
	private final String key;
	
	//要么直接指定初始化值，要么在“所有”的构造函数中指定
	public StatusTest(){
		key="";
	}
	
	
	
	//countdownlatch cyllicbarrier
	public static void main(String[] args){
		
		Calculator runnable = new Calculator(1);
		for(int i=0;i<5;i++){
			Thread t = new Thread(runnable,"ruunable-"+i);
		}
		
	}
	
	
	
	
	private static class Calculator implements Runnable{
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
