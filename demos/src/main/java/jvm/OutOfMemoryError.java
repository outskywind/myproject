package jvm;

import org.junit.Test;

public class OutOfMemoryError {
	
	@Test
	public void testClGetResource() {
		int i=0;
		while(true) {
			MyThread.getInstance();
			System.out.println(++i);
	}
	}
	
	
	//这里是单例模式，这个类不会引起内存溢出，否则会溢出
	private static class MyThread implements Runnable {
		private static MyThread cacheMyThread;
		
		
		MyThread(){
			Thread thread = new Thread(this, "tt-thread");
			thread.start();
		}
		public static MyThread getInstance(){
			if(cacheMyThread==null) {
				cacheMyThread = new MyThread();           
			}
			return cacheMyThread;
		}
		public void run() {
			ClassLoader classLoader =  Thread.currentThread().getContextClassLoader();
			classLoader.getResource("hello.xml");
			
		}
	}

}
