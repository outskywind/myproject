package concurrency.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class Iterators {
	
	
	//不是线程安全的
	private  List<Integer> list = new ArrayList<Integer>();
	
	@Test
	public void testIteratorConcurrent(){
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		
		Collections.sort(list);
	}
	
	

}
