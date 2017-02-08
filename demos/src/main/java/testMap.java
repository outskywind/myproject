import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;


public class testMap {
	public static void main(String[] args ) {
	       String a = new String("q");
	       String b = "ss";
	       List l = new ArrayList();
	       Map m = new HashMap();
	       m.put("str", a);//此时存的是a的变量引用，也就是"q"的变量引用
	       m.put("strb", b);
	       m.put("list", l);
	       b="ss";//java中=是将对象引用赋给了变量b，此时b的变量引用已经变成了"ss"的引用。
	       l.add(a);
	       a="ss";
	       System.out.println(a);
	       System.out.println("List after change "+l);
	       System.out.println("Map after change "+m.get("str"));//str 存的是String对象的值
	       System.out.println(m.get("list"));//list 存的是List对象
	       
	       Double double1 = new Double(11);
	       
	       m.put("double", double1);
	       
	       double1 = new Double(22);
	       
	       System.out.println("Double changed "+m.get("double"));
	}
	
	
	public static void testTreeMap() {
		Map tm = new TreeMap();
		
	}
	
	//静态类
	private  static class Student implements Comparable<Student>{
		public String name;
		public double score;
		
		public int compareTo(Student o) {
			//double score = Double.parseDouble(o.score);
			if(this.score>o.score) {
				return 1;
			} else if(this.score<o.score){
				return -1;
			}
			return 0;
		}
	}
	
	public void testInnerClass() {
		testMap out  = new testMap();
		
		
	}
	
	public void testSynchronizedTreeMap() {
		
		//SortedMap sMap = Collections.synchronizedSortedMap(new TreeMap<K, V>(comparator));
		
	}
	
	@Test
	public void testBoolen() {
		Map rMap = new HashMap();
		boolean b = true;
		rMap.put("b", b);
		boolean bv = (Boolean)rMap.get("b");
		Boolean cv = (Boolean)rMap.get("c");
		System.out.print(bv);
		System.out.print("cv="+cv);
	}
	
}
