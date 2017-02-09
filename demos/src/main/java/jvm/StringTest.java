package jvm;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StringTest {
	
	@Test
	public void testIntern6() {
		//String str1 = "hello world";
		String str2 = new String("hello world2");
		System.out.println(str2.intern()=="hello world2");
		//在 jdk6中，string intern 将返回常量池中这个字符串常量的引用。
		//对于第一次遇到的字符串 intern 先在永久代的常量池拷贝一个
		List holder = new ArrayList();
		int i=0;
		while(true) {
			holder.add(new StringBuffer("hello").append(i++).toString().intern());
			
		}
	}
	
	@Test
	public void testIntern6_2() {
		//在 jdk6中，string intern 将返回常量池中这个字符串常量的引用。
		//对于第一次遇到的字符串 intern 先在永久代的常量池拷贝一个
		
		int i=0;
		while(true) {
			new StringBuffer("hello").append(i++).toString().intern();
		}
	}

}
