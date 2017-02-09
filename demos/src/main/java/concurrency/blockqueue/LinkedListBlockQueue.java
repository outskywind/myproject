package concurrency.blockqueue;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LinkedListBlockQueue<E> implements BlockQueue<E>{
	
	//必定是独占锁
	private List< E> list=null;
	
	
	
	public LinkedListBlockQueue(){
		this.list = new LinkedList<E>();
	}
	

	@Override
	public void put(E e) {
		
		synchronized (list) {
			list.add(e);
			list.notify();
			System.out.println("Thread "+Thread.currentThread().getId()+" notified");
		}
	}

	@Override
	public   E  pop(long index) {
		//如果没有就线程休眠，释放锁
		synchronized (list) {
		if(list.isEmpty()){
			try {
				System.out.println("thread "+Thread.currentThread().getId()+"before waits");
				list.wait();
				System.out.println("thread "+Thread.currentThread().getId()+"after waits");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(!list.isEmpty()){
			return  list.remove(0);
		}
		return null;
	}
	}

	@Override
	public void remove(long index) {
		
	}
	
	/**
	 * Type是不能实例化的，所以线程内部需要实例化的
	 * ，不能使用Type
	 * @author johnconner
	 *
	 */
	class Worker implements Runnable{
		
		private LinkedListBlockQueue  queue;
		
		public Worker(LinkedListBlockQueue<E> queue){
			this.queue = queue;
		}
		
		@Override
		public void run() {
			
			//
			Random random = new Random(System.currentTimeMillis());
			//makes some Objects
			while(true){
				Integer i = Integer.valueOf(random.nextInt());
				////需要在编译时确定Type 的具体值，Type 不能实例化
				queue.put(i);
				try {
					//sleep不释放锁，占有锁等待指定时间，但是仍然可以被cpu给调度出去(时间片结束)
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	//当对象被传递给线程之后，只要线程没有销毁，对象就永远不会被回收
	class Customer implements Runnable{
		
		private LinkedListBlockQueue queue;
		
		
		public Customer(LinkedListBlockQueue<E> queue){
			this.queue = queue;
		}

		@Override
		public void run() {
			
			while(true){
				Integer ing  = (Integer)queue.pop(0);
				System.out.println(ing);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void start(){
		
		 for(int i=0;i<3;i++){
			 //这才是正确的启动多线程的方式
			new Thread(new Worker(this)) .start();
			new Thread(new Customer(this)) .start();
			//这只是单纯的方法调用，并不会启动新的线程
			//  new Customer(this).run();
		  }
		
	}
	
	
	
	  public  static void  main(String[] args){
		
		  LinkedListBlockQueue<Integer> queue = new LinkedListBlockQueue<Integer>();
		  //
		 queue.start();
		  
		  
	}
	
	

}
