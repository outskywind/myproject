package algorithm.sort;

import java.io.Serializable;
import java.util.Random;
public class SortUtil implements Serializable{
	
	private static final long serialVersionUID = -1L;
	private static int[] array = new int[10];
	
	static {
		Random  sid = new Random(10);
		for(int i=0;i<array.length;i++) {
			array[i]= sid.nextInt();
		}
	}
	
	//插入排序,上界下标length-1
	public static  void sort() {
		for(int i=0;i<array.length-1;i++) {
			for(int key = i+1;(key>0)&&(array[key]<array[key-1]);key--) {
				if(array[key]<array[key-1]) {
					int tmp = array[key];
					array[key] = array[key-1];
					array[key-1] = tmp;
				}
			}
		}
	}
	
	//插入排序,上界下标length-1
		public static  void sort1() {
			//待插入的数
			for(int i=0;i<array.length;i++) {
				int end = i-1;
				while(end>=0 && array[end+1]<array[end]) {
					int tmp = array[end];
					array[end] = array[end+1];
					array[end+1] = tmp;
					end--;
				}
			}
		}
	
	
	public static void sort2() {
		for(int i=0; i<array.length;i++) {
			//每次从0开始
			for(int j=0;j<array.length-i-1;j++) {
				if(array[j]>array[j+1]) {
					int tmp = array[j];
					array[j] = array[j+1];
					array[j+1] = tmp;
				}
			}
		}
	}
	
	public static void mergeSort() {
		merge_sort(array,0,array.length-1);
	}
	
	private static void merge_sort(int[] array,int start,int end) {
		if(end<=start) {
			return ;
		}
		//end-start 对于key+1,end来说会越来越大
		int key = (int)Math.ceil((end+start)/2);

		merge_sort(array, start,key);
		merge_sort(array, key+1,end);
		
		//合并
		int length = end-start+1;
		int left=start;
		int right=key+1;
		int[] sorted = new int[length];
		for(int i=0;i<length;i++) {
			
			if(left <=key && right<=end) {
				if(array[left]<array[right]) {
					sorted[i] = array[left++];
				} else {
					sorted[i] = array[right++];
				}
			}
			else if(left<=key) {
				sorted[i] = array[left++];
			}
			else {
				sorted[i] = array[right++];
			}
		}
		
		System.arraycopy(sorted, 0, array, start, sorted.length);
		return;
	}
	
	public static void printResult() {
		for(int i=0;i<array.length;i++) {
			System.out.println(array[i]);
		}
	}
	
	public static void main(String[] args) {
		SortUtil.mergeSort();
		SortUtil.printResult();
		
	}
	
}
