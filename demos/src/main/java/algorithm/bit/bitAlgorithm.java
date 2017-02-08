package algorithm.bit;

import org.junit.Test;

public class bitAlgorithm {
	
	@Test
	public void testBit2N() {
		for(long i=0;i<Long.MAX_VALUE;i++) {
			//long 做位运算
			if( (i&(i-1))==0 ) {
				System.out.println("i="+i);
			}
		}
		
	}
	
	@Test
	public void test2() {
		long start=System.currentTimeMillis();
		for(long i=0;i<Long.MAX_VALUE;i++) {
		}
		long end=System.currentTimeMillis();
		System.out.println("cost time="+(end-start));
	}
	
	@Test
	public void test1() {
		int i=1;
		i<<=4;
		
		System.out.println(i);
		
	}

}
