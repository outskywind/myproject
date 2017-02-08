package concurrency.threadlocal;

public class ObjectThread extends Thread {
	
	//在这个方法中仍然是父线程调用，
	//因此这个变量仍然是设置在了父线程中
	//因此只能每次在run方法中设置
	public ObjectThread(){
		//Threadlocaltest.init();
		}
	
	
	/**run方法中的代码才是子线程开始的地方,
	*如果是ThreadLocal 对象，即使是static 也无法实现对象的多线程共享，
	*ThreadLocal 真正的含义是这个变量只能存在于当前的这个线程空间，
	*当前线程终止，则这个变量即使是static的，也会GC消失。
	*因此当想让这个变量在多个线程之间共享，传递状态,那么还是要同步机制实现；则不要设定为ThreadLocal的，这个是线程私有的意思
	**/
	@Override
	public void run(){
		//只能这样
		if(Threadlocaltest.getMember()==null){
			Threadlocaltest.init();
		}
		
		//必须要在每个线程中自己设置变量
		System.out.println("thread"+Thread.currentThread().getId()+" ="+Threadlocaltest.getMember().get());
		
		for(int i=0;i<3;i++){
			Threadlocaltest.getMember().set(i+1);
		}
		
		
	}

}
