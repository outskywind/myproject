package jvm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class LoaderTest {
	private  String  path="D:\\Users\\quanchengyun970\\Desktop\\weblogic-without-ant\\weblogic-without-ant.jar";
	
	
	@Test
	public void testxx() {
		try {
			URL[] urls = {new File(path).toURI().toURL()};
			MyClassLoader loader = new MyClassLoader(urls);
			String clazz = "weblogic.apache.xalan.processor.TransformerFactoryImpl";
			Class class1 = loader.loadClass(clazz);
			System.out.println(class1.getCanonicalName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private class MyClassLoader extends URLClassLoader{

		public MyClassLoader(URL[] urls) {
			super(urls);
		}
		
	
		
	}

}
