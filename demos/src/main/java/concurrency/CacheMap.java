package concurrency;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class CacheMap {
	
	private static AtomicInteger lockObj =new AtomicInteger();
	
	private static Lock lock = new ReentrantLock();
	
	public static interface Computable<K,V>{
		public V compute(K arg);
	}
	//缓存封装类
	//只是作为调度框架，并不进行实际的逻辑计算
	private static class CacheWrapper<K,V> {
		
		private  ConcurrentHashMap<K, FutureTask<V>> cache = new ConcurrentHashMap<K, FutureTask<V>>();
		/**
		 * 业务计算逻辑
		 */
		private final Computable<K, V> computable ;
		
		public CacheWrapper(Computable<K,V> _computable){
			this.computable = _computable;
		}
		
		/**
		 *  blocks util result is avaialable
		 * @param arg
		 * @return
		 */
		public V compute(final K arg) {
			//因为该任务计算可能会被中断，而需要重新跑
			while(true){
				FutureTask<V> f = cache.get(arg);
				if(f==null){
					FutureTask<V> ft =  new FutureTask<V>(new Callable<V>() {
						@Override
						public V call() throws Exception {
							//Thread.currentThread().wait(2000L); shit the wait is used for objects , not Thread static method
							System.out.println("Thread"+Thread.currentThread().getId()+" start wait()--- at " + new Date(System.currentTimeMillis()));
							lock.lock();
							//lock.wait(2000L);
							lock.wait(2000L);
							lock.unlock();
							System.out.println("Thread"+Thread.currentThread().getId()+" end wait()--- at " + new Date(System.currentTimeMillis()));
							return  computable.compute(arg);
						}
			});
					f = cache.putIfAbsent(arg,ft);
					if(f==null){
						f=ft;
					}
				}
				try {
					//blocks util result available
					System.out.println("before run future task----");
					f.run();
					return f.get();
				} catch (InterruptedException e) {
					//该任务被中断，就从缓存移除，因为FutureTask只能run一次
					//那么需要重启任务
					e.printStackTrace();
					//cache.remove(arg, f);
					break;
				} catch (ExecutionException e) {
					e.printStackTrace();
					break;
				}catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			return null;
		}
	}
	
	@Test
	public void testRun(){
		System.out.println("Thread"+Thread.currentThread().getId()+" start---");
		Computable<String, Integer> computer = new Computable<String, Integer>() {
			@Override
			public Integer compute(String arg) {
				//try {
					//System.out.println("Thread"+Thread.currentThread().getId()+" start wait()--- at " + new Date(System.currentTimeMillis()));
					//Thread.currentThread().wait(2000L); shit the wait is used for objects , not Thread static method
					
					//lock.wait(2000L);
					//System.out.println("Thread"+Thread.currentThread().getId()+" end wait() --- at " + new Date(System.currentTimeMillis()));
				//} catch (InterruptedException e) {
					//e.printStackTrace();
				//}
				System.out.println("Computable interface called---  ");
				return Integer.valueOf(arg) *12;
			}
		};
		
		CacheWrapper<String, Integer> cachemap = new CacheWrapper<String, Integer>(computer);
		
		Integer value = cachemap.compute("123");
		System.out.println(value);
		
	}

}
