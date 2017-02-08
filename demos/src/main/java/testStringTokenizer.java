import java.util.Random;
import java.util.StringTokenizer;

public class testStringTokenizer {
	static int[] indx = new int[10];
	static StringBuffer sbBuffer = new java.lang.StringBuffer();
	public static void main(String[] args) {
		for(int i=0;i<indx.length;i++) {
			int number = new Random().nextInt();
			if(sbBuffer.length()>2) {
				sbBuffer.append(".");
			}
			sbBuffer.append(number);
		}
		String str= sbBuffer.toString();
		System.out.println("sssssssssss:"+str);
		
		long start=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			String[] sarry =  str.split("\\.");
		}
		long end = System.currentTimeMillis();
		System.out.println(" spilt cost time = " +(end-start));
		
		long start2=System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			StringTokenizer stringTokenizer = new StringTokenizer(str,".");
			for(;stringTokenizer.hasMoreTokens();) {
				stringTokenizer.nextToken();
			}
		}
		
		long end2 = System.currentTimeMillis();
		System.out.println(" stringTokenizer cost time = " +(end2-start2));
	}
	
	

}
