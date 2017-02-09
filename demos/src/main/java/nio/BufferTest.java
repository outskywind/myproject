package nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

import oracle.sql.CharacterBuffer;

import org.junit.Test;

import sun.util.logging.resources.logging;

public class BufferTest {
	
	
	@Test
	public void test(){
		
		ByteBuffer bbf = ByteBuffer.allocate(9);
		ByteBuffer bf2 = ByteBuffer.allocate(9);
		
		
		byte[] str1 = {'c','o','m','l','o'};
		
		byte[] str2 = {'m','e','l','l','o'};
		
		bbf.put(str1).flip();
		
		bf2.put(str2).flip();
		
		
		
		//比较的是position剩下的内容到limit为止
		//字典序在前的小于后面的
		System.out.println(bbf.compareTo(bf2));
		
		char ch = 'h';
		
		long l = 2^64-1;
		
		char ch2 = (char)l;
		
		int  i = ch2;
		
		
		//java 最大的整形是long,64位,有符号的
		//计算数，java无法静态检查判断，自动截取？
		//java中 ^ 位运算，异或
		int ll = (int)Math.pow(2.00, 30)-1+(int)Math.pow(2.00, 30);
		
		System.out.println(ll);
		
		
		//默认直接数都是int,0xFFFFFFFF是直接数，因此只能表示int;
		long l3 = 0xFFFFFFFF;
		
		char  ch3 =(char) 0xFFFFFFFF;
		
		double  bits = Math.log((double)ch3+1)/Math.log(2.00);
		
		//强转是直接截位
		int i2= (int)1.99999999;
		System.out.println(i2);
		
		
		System.out.println("char length = "+bits/8+" bytes");
		
		
		CharBuffer charBuffer  = CharBuffer.wrap("Hello,world");
		//charBuffer.flip();
		System.out.println("charBuffer="+charBuffer);
		
		charBuffer.position(0).limit(5);
		
		//这个是复制一个新的buffer，原来buffer不变，但是数组是同一个
		//其他标志不一样
		CharBuffer cdepBuffer  = charBuffer.slice();
		
		//压缩本buffer,设置成可以继续put的状态，在连续的读写转换中很有用
		cdepBuffer.compact();
		
		System.out.println("slice buffer = "+cdepBuffer);
		
		// ByteBuffer 例外，是小端
		ByteOrder boByteOrder  = cdepBuffer.order();
		
		System.out.println(boByteOrder.toString());
		
		
		byte b = 15;
		byte[] ba = new byte[8];
		for(int k=0;k<8;k++){
			ba[7-k] = (byte) (b & 1);
			b=(byte) (b>>1);
			
		}
		//默认的基本型都是big_endian
		for(int j=0;j<ba.length;j++){
			System.out.print(ba[j]);
		}
		System.out.print("\n");
		
		ByteBuffer bf1 = ByteBuffer.allocate(7).order(ByteOrder.BIG_ENDIAN);
		
		//这是一个准备读出的buffer
		CharBuffer cb1 = bf1.asCharBuffer();
		
		System.out.println("cap:"+cb1.capacity()+"limit:"+cb1.limit());
		
		int iii = 0xFFFFFFFF;
		
		//java扩展是符号扩展，要保持扩展后值不变
		long lint  = iii;
		
		long l_u_int = 0xFFFFFFFFl & iii ;
		
		System.out.println(lint);
		System.out.println(l_u_int);
	}
	
	
	@Test
	public void testMethods(){
		
		
		ByteBuffer bbf= ByteBuffer.allocate(16);
		
		/**
		 * pos=0,
		 * limit=cappacity
		 * 所以是用来请空，准备写入的
		 */
		bbf.clear();
		/**
		 * limit=pos;
		 * pos=0;
		 * 所以是为读取做准备
		 */
		bbf.flip();
		/**
		 * pos=0;
		 * 这个不改变limit;
		 * 重复读取之间用
		 */
		bbf.rewind();
		/**
		 *不改变原来buf的标记
		 */
		bbf.slice();
		/**
		 * 这个是压缩本身用的，并不创建新的buffer
		 * 最终pos=limit+1;
		 * limit=capacity;
		 * 读取压缩之后继续写入用
		 */
		bbf.compact();
		
	}
	

}
