package jvm;


public class MemoryGC {
	public static int _1MB =1024*1024;
	

	/**
	 * -Xms20m -Xmx20m -Xmn10m -verbose:GC  -XX:+PrintGCDetails -XX:SurvivorRatio=8 
     // -XX:MaxTenuringThreshold=1
	 */
	public void testGc() {
		byte[] var1 = new byte[_1MB/4];
		
		byte[] var2 = new byte[4*_1MB];
		//first GC,then allocate in eden
		byte[] var3 = new byte[4*_1MB];
		//Next GC
		byte[] var4 = new byte[4*_1MB];
	}

	public static void  main(String[] args){
		MemoryGC gc = new MemoryGC();
		gc.testGc();
		try {
			Thread.sleep(600000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
