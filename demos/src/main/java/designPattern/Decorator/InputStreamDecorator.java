package designPattern.Decorator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class InputStreamDecorator {
	
	@Test
	public void test0(){
		try {
			BufferedInputStream bufStream = new BufferedInputStream(new FileInputStream(new File("")));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void test(){
		try {
			FileReader  fReader = new FileReader(new File(""));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUTF8(){
		//java  内存中字符是用unicode 字符集的 2字节
		// unicode字符集编码成utf-8时，一般会变长
		char ch2 = '𠣗'; //哈哈，如果这个字符unicode二进制码值超过了 2字节范围，那么用char表示还是会报错的
		String ch3 = "再";//java 内存中是unicode编码
		char ch = '一';
		int chh =ch;
		System.out.println(chh & 0xffff);
		for(int i=0;i<16;i++){
			//无符号>>>
			System.out.print((chh & 0x8000)>>>15);
			//位移操作会转成int再操作的
			chh = chh<<1;
		}
		System.out.println("");
		try {
			//中文utf8编码格式是3字节
			//但是他的二进制值是2字节以内的
			byte[] b2 = "一".getBytes("utf-8");
			
			for(byte b:b2){
				int chhh = b;
				for(int i=0;i<8;i++){
					System.out.print((chhh & 0x80)>>>7);
					//位移操作会转成int再操作的
					chhh = chhh<<1;
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
