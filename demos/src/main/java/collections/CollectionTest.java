package collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class CollectionTest {
	
	
	
	class Person{
		private int age;
		private String firstName;
		
		public int getAge(){
			return this.age;
		}
		public void setAge(int age){
			this.age = age;
		}
		public Person(int age, String fname){
			this.age = age;
			this.firstName = fname;
		}
		
		@Override
		public String toString(){
			return this.firstName;
		}
		
	}
	
		
		
	class Compatetor implements Comparator<Person>{

		@Override
		public int compare(Person o1, Person o2) {
			return o2.getAge() - o1.getAge();
		}
		
	}
	
	@Test
	public void testCompatetor(){
		
		Person p1 = new Person(10, "brother");
		Person p2 = new Person(8, "littleBrother");
		Person p3 = new Person(4, "youngSister");
		
		List<Person> aList = Arrays.asList(p1,p3,p2);
		
		System.out.println(aList);
		
		//sort
		java.util.Collections.sort(aList, new Compatetor());
		
		System.out.println("sorted:"+aList);
		
		Person key =  new Person(8, "");
		//只会根据Comparator 中进行排序的字段进行比较，
		//而不是对象本身，那么其实找到的是含有与这个对象的字段的值相同的字段的对象
		int index = Collections.binarySearch(aList, key,new Compatetor());
		
		System.out.println(index+" is  "+ aList.get(index));
		
	}

}
