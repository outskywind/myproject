package concurrency.blockqueue;

import java.util.LinkedList;

/**
 * 使用工厂模式解决
 * @author johnconner
 *
 */
public class BlockedQueue<E> {
	
	//2种链表
	//private List<E> list = null;
	/**
	 * 也就是说内部类必须的通过外部类的实例来访问。
	 * static 是可以直接调用的，那么必须得有外部类实例才行
	 * @param e
	 * @return
	 */
	public static  <E> BlockedQueue<E> getLinkedBockQueue(E e){
		
		 //
		return  new LinkedBlockedQueue();
	}
	
	public static class LinkedBlockedQueue<E> extends BlockedQueue<E> {
		
		
		private LinkedList<E> list = null;
		
		public LinkedBlockedQueue(){
			this.list = new LinkedList<E>();
		}
		
	}
	
	
	//阻塞队列的添加
	private Object jok =  new Object();
	{
        this.jok.equals(null);
	}
	

}

