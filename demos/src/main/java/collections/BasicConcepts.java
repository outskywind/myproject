package collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class BasicConcepts {
	
	
	@Test
	public void test(){
		
		//
		
		ArrayList<Integer> alist = new ArrayList<Integer>();
		
		System.out.println(alist);
		
		List<Integer> list = new ArrayList<Integer>();
		
		Set<Integer> set = new HashSet<Integer>();
		
		Map<String, Integer> map = new HashMap<String,Integer>();
		
		Collections.synchronizedList(list);
		
	}

}
