package collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

import org.junit.Test;

public class TreeSetTest {
	
	
	
	class Item implements Comparable {
		private long value ;
		
		public Item(long value){
			this.value=value;
		}
		
		public Item(){
			this.value=0L;
		}
		
		public long getValue(){
			return this.value;
		}

		@Override
		public int compareTo(Object o) {
			Item o1 = (Item)o;
			
			if(this.value>o1.getValue())
				return 1;
			if(this.value<o1.getValue()){
				return -1;
			}
			return 0;
		}
		
	}
	
	@Test
	public void test(){
		
		//logn的时间复杂度
		TreeSet<Item> ts = new TreeSet<TreeSetTest.Item>();
		
		Random  random = new Random(1000L);
		for(int i=0;i<1000;i++){
			Item item = new Item(random.nextLong());
			ts.add(item);
		}
		
		//遍历
		Iterator it = ts.iterator();
		while(it.hasNext()){
			Item item = (Item)it.next();
			System.out.println(item.getValue());
		}
		
		//新的array
		Item[] items = (Item[]) ts.toArray();
		
		
		
		ArrayList arrayList  = new ArrayList();
		
	}
	
	
	

}
