package concurrency;

import java.util.concurrent.atomic.AtomicLong;

import concurrency.blockqueue.LinkedListBlockQueue;


/**
 * 原子操作的线程安全类
 * @author johnconner
 *
 */
public class AtomicClassObject {
	
	//原子类
	private final AtomicLong atm_counter=new AtomicLong(0) ;
	
	//共享变量,放入线程安全的 queue.如果不需要延迟初始化的话，尽量直接初始化
	private static LinkedListBlockQueue<Long> queue = new LinkedListBlockQueue<Long>();
	
	private  static LinkedListBlockQueue<Integer> getObject(){
		//双重检查是不可靠的
		//原因在于重排序
		return new LinkedListBlockQueue<Integer>();
		
	}
	
	/**
	 * lazyinitHolder模式
	 * @author johnconner
	 *
	 */
	//但是需要延迟初始化的情况，比如一个连接池的连接
	//内部类必须要是static的才能有static成员
	private  static class LazyInitHolder{
		private final static LinkedListBlockQueue<Integer> lazy_init_queue = getObject();
	}
	
	public static  LinkedListBlockQueue<Integer> getLazyQueue(){
		
		return LazyInitHolder.lazy_init_queue;	
	}
	
	
	
	protected class Request implements Runnable{

		@Override
		public void run() {
			
			while(true ){
				
				long counts = atm_counter.incrementAndGet();
				
				queue.put(Long.valueOf(counts));
				
			}
		}
	}
	
	
	//
	public void start(){
		
		for(int i=0;i<3;i++){
			Thread t1 = new Thread(new Request());
			t1.start();
		}
		
	}
	
	public void stop(){
		
	}
	
	
	
	public static void main(String[] args){
		//
		AtomicClassObject ato = new AtomicClassObject();
		
		//中断线程
		
		
		
	}
	
	

}
