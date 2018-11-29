package algorithm.bit;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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


	//一定要小心
	static long mask32Bit  = (1L<<32)-1;

	@Test
	public void testsplit(){
		long data = 4634123;
		int high = (int)((data >>>32) & mask32Bit);
		int low = (int)(data & mask32Bit);
		IntBuffer dataBuff = ByteBuffer.allocate(8).asIntBuffer();
		dataBuff.put(high).put(low);
		dataBuff.flip();
		//System.out.println(dataBuff.get()+" "+dataBuff.get());
		System.out.println(data ==((((long)dataBuff.get())<<32)+dataBuff.get()));

	}

}
