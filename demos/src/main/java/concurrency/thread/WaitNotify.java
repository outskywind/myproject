package concurrency.thread;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class WaitNotify {

	// 对象监视器锁
	private static Object lock = new Object();

	static final int size = 6;
	private static Queue<Integer> queue = new ArrayBlockingQueue<Integer>(size);
	
	private static int producerCount=1;

	
	public static void main(String[] args){
		Consumer consumer = new Consumer();
		Producer producer = new Producer();
		Thread cThread = new Thread(consumer, "consumer");
		Thread pThread = new Thread(producer, "producer1");
		Thread p2Thread = new Thread(producer, "producer2");
		
		cThread.start();
		p2Thread.start();
		pThread.start();
	}
	
	
	
	
	
	static class Consumer implements Runnable {
		public void run() {
			while(true){
				try {
					Thread.sleep(10);
					synchronized (lock) {
						//循环条件等待
						while (queue.isEmpty()) {
							//条件不满足释放锁，挂起阻塞？
							//直到有线程在同一个监视器锁对象上发起notify才会唤醒,但是唤醒的线程需要再次获取这个对象监视器锁
							//此时此线程没有运行了
							System.out.println("empty ,call wait " +Thread.currentThread().getName());
							lock.wait();
							System.out.println("notified:  "+Thread.currentThread().getName());
						}
						//
						Integer i = queue.poll();
						//不知道谁会获取到锁，所以只能全部唤醒
						lock.notifyAll();
						System.out.println("get "+i.intValue());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	static class Producer implements Runnable {
		public void run() {
			while(true){
				try {
					Thread.sleep(100);
					synchronized (lock) {
						//循环条件等待
						while (queue.size()>=size) {
							//条件不满足释放锁，挂起阻塞？
							//直到有线程在同一个监视器锁对象上发起notify才会唤醒
							//此时此线程没有运行了,
							System.out.println("full ,call wait " +Thread.currentThread().getName());
							lock.wait();
							System.out.println("state:  " + Thread.currentThread().getState());
							System.out.println("notified:  "+Thread.currentThread().getName());
						}
						//
						for(int i=0;i<6;i++){
							producerCount++;
							boolean ok = queue.offer(Integer.valueOf(producerCount));
							System.out.println("offer add  "+ ok);
						}
						//不知道谁会获取到锁，所以只能全部唤醒,全部唤醒的线程将会竞争锁，不会被挂起，进入等待锁状态【会一直等待下去嘛？】
						lock.notifyAll();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
