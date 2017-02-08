package designPattern.singleton;

public class Factory {
	
	private static Factory factory  ;
	
	private static Object lock = Factory.class;
	
	private int vip=0;
	
	private Factory() {
		vip=1;
	}
	
	
	public static Factory getInstance() {
		
		synchronized(lock) {
			if(factory==null) {
				factory=new Factory();
			}
		}
		
		return factory;
	}
	
	 public void record() {
		System.out.println(vip++) ;
	 }
	
	

}
