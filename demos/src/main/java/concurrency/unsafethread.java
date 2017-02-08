package concurrency;


public class unsafethread {
	private final int[] lastnumber = new int[100];
	private final int[] factors= new int[100];
	private static int countsize;//static 是针对对象的而言，对象的所有实例共用一个static成员
	public void getfactor(int number) {
		if(countsize==0 ||countsize>0 && lastnumber[countsize-1]!=number) {
			countsize++;
			factors[0]=number-1;
			factors[1]=number+1;
			lastnumber[countsize-1]=number;
			
		}
		System.out.println(countsize+","+number+","+factors[0]+","+factors[1]);
	}
	
}
