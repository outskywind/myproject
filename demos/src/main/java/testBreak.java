
public class testBreak {
  public static void main(String[] args ) {
	  //break 结束循环语句块，跳出循环
	  //break 当然只会结束本层循环
	  for(int i=0;i<100;i++){
		  for(int j=0;j<10;j++) {
			  if (i%10 == 0 ){
				  System.out.println("inner break+"+j);
				  break;
			  } 
		  }
		  if (i%10 == 0 ){
			  System.out.println("outer break+"+i);
			  break;
		  } 
		  System.out.println("no_break+"+i);
	  }
	  //continue 结束循环语句块，重新开始循环
	  for(int i=0;i<100;i++){
		  if(i>1){
		  if (i%10 == 0 ){
			  System.out.println("continue+"+i);
			  continue;
		  } 
		  }
		  System.out.println("no_continue+"+i);
		  
		  int[] out = new int[]{4,5,6};
		  test(out);
		  System.out.println(out[0]);
		  System.out.println(out[1]);
		  System.out.println(out[2]);
	  }
  }
  
  public static void test(int[] x){
	  x = new int[]{1,2,3};
	 }
}

