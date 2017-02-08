package concurrency.blockqueue;


public class SleepTest {
	private volatile Boolean isCallable =false;
	
	class LongSleeper implements Runnable{
		
		@Override
		public void run() {
			while(true){
				try {
					System.out.println("i am going to sleep for 10s");
					Thread.sleep(10000);
					System.out.println("i sleeped well for 10s");
				} catch (InterruptedException e) {
					//睡眠中被唤醒
					//interrupted status 被清除了，不能在这个异常里面用 Thread.interrupted() 判断。
					System.out.println("it was interrupted to working agian");
				}
			}
		}
	}
	
	/**
	 * 内部类的好处是可以访问外部类的私有成员
	 * @author johnconner
	 *
	 */
	class Caller implements Runnable{
		
		private Thread sleeper ;
		//private Boolean isCallable;
		
		public Caller(Thread sleeper ,Boolean isCallable){
			this.sleeper = sleeper;
			//this.isCallable = isCallable;
		}

		@Override
		public void run() {
			
			while(true){
				if(isCallable){
					//唤醒休眠的线程
					System.out.println("isCallable ture,inteerrupt the sleeper");
					sleeper.interrupt();
					//每次都能正确的设置，不管中间这isCallable 是否被改为true，这一步都将会改成false。
					//也就是说，不依赖这个变量的当前值。满足这一条，可用volatile
					isCallable=false;
				}
				else{
					System.out.println("isCallale not true");
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public void start(){
		
		//首先启动线程
		Thread sleeper = new Thread(new LongSleeper());
		Thread caller = new Thread(new Caller(sleeper, this.isCallable));
		sleeper.start();
		caller.start();
		while(true){
			if(!this.isCallable){
				this.isCallable=true;
				System.out.println("isCallable setted");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			/**
			else{
				System.out.println("isCallable is true by outter Thread");
			}
			**/
		}
	}
	
	public static void main(String[] args){
		
		SleepTest sl = new SleepTest();
		sl.start();
		
	}

}
