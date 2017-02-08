package nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.junit.Test;


public class FileChannelTest {
	
	
	@Test
	public void test(){
		
		try {
			//gbk编码一个字符是一个byte
			//一个汉字是2个byte,file 读取的是二进制编码，也就是byte[]字节数组
			FileChannel fc = new FileInputStream("D:\\logs\\qcy\\app.log").getChannel();
			// lock 无参数是独占锁
			FileLock flock = fc.lock(0L,fc.size(),true);
			System.out.println(flock.isShared());
			//释放锁
			
			//正确用法，在byteBuffer创建之后，应立即创建视图。切记
			//
			ByteBuffer bb= ByteBuffer.allocate(256);
			IntBuffer ib = bb.asIntBuffer();
			//切记，读进来的是字符对应的编码啊！！
			//‘5'的的编码转换成byte后的编码值是53啊
			int reads =fc.read(bb,0);
			System.out.println("read bytes="+reads);
			
			//此处的问题？,共用一个array,新buffer 无自己的array
			//以当前的position到剩下的位置截取成新的视图
			//IntBuffer ib = bb.asIntBuffer();
			//bb.flip();
			
			
			System.out.println("pos="+ib.position()+" ,imit="+ib.limit());
			int value =ib.get(0);
			System.out.println("the  0 th int="+value);
			for(int i=0;i<4;i++){
				byte bytevalue = (byte) (value&0xFF);
				value = value>>8;
			    char ch  = (char) bytevalue;
				System.out.print(ch);
			}
			flock.release();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
