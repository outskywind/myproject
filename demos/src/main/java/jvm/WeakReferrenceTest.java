package jvm;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.junit.Test;
public class WeakReferrenceTest {
	
	
	@Test
	public void testXX() {
		HashMap<String,String> hashMap = new HashMap<String,String>();
		WeakReference<HashMap<String, String>> wkr = new WeakReference<HashMap<String, String>>(hashMap);
		
		System.gc();
		
		System.out.println(wkr.get());
		
		System.gc();
		
		System.out.println(wkr.get());
	}





}
