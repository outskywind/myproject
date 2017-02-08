
/**
 * merge排序统计逆序对
 * 
 * 
 **/

public class Count {
	
	public static int count(int[] a,int left,int right) {
		int key=(int) Math.floor((left+right)/2);
		int count_left=0;
		int count_right=0;
		if(left<right) {
			count_left=count(a,left,key);
			count_right=count(a, key+1, right);
			return count_left+count_right+mergecount(a,left,key,right);
		}
		return 0;
	}
	
	public static int mergecount(int[] a,int left,int key,int right) {
		int count=0;
		int[] sorted = new int[right-left+1];
		int j=left;
		int k=key+1;
		for(int i=0; i<sorted.length;i++) {
			//a[j]>a[k],则 a[j],a[j+1]...都大于a[k]
			if(j<=key && k<=right) {
				if(a[j]>a[k]) {
					sorted[i]=a[k];
					for(int l=j;l<=key;l++) {
					System.out.println("<"+a[l]+","+a[k]+">");
					}
					k++;
					//? 统计的是逆序对
					count=count+key-j+1;
				} else {
					sorted[i]=a[j];
					j++;
				}
			} else {
				if(j>key) {
					sorted[i]=a[k];
					k++;
				} else if(k>right){
					sorted[i]=a[j];
					j++;
					}
				}
			}
		for(int i=left;i<=right;i++) {
			a[i]=sorted[i-left];
		}
		return count;
	}
	
	public  static void main(String[] args) {
		int[] array = {1,4,2,7,5,3,54,21,34,6,32,8,3,13};
		int count = Count.count(array, 0, 13);
		System.out.println("count = "+count);
		for(int i=0;i<array.length;i++) {
			System.out.print(array[i]+" ");
		}
		
	}

}
