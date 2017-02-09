package concurrency.blockqueue;

public interface BlockQueue<E> {
	
	
	public  void put(E e);
	
	public  E pop(long index );
	
	public void remove(long index);
	

}
