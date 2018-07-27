import org.junit.Test;

public class programma {

	public static void main(String[] args) {
		int[] out = new int[]{4,5,6};
		test(out);
		System.out.println(out[0]);
		System.out.println(out[1]);
		System.out.println(out[2]);
		
		String str="abc";
		//会拷贝一份str的副本。
		String str2=str.replaceAll("a", "B");
		System.out.println(str);
		int a=0;
		int b=a;
		b=1;
		System.out.println(a);
	}
	 //java传值，准确的说是传栈里的值。对象变量在站立保存的是堆的引用的值。
	 public static int[] test(int[] x){
		  x = new int[]{1,2,3};
		  return x;
	}
	 
	 public  void testString() {
		 String str="abc";
		 str.replaceAll("a", "B");
		 System.out.println(str);
		 
	 }
	 @Test
	 public void testR(){
	 	String str = "中国平安\n" +
				"保险股份有限公司深圳分公司";

	 	System.out.println(str.replaceAll("\n"," "));
	 }

	 @Test
	public void testX(){
	 	int key = 10;
	 	switch (key){
			case 1:
				//continue; //continue 只能在循环里使用
			case 2:
				break;
			default:
		}
	 	System.out.println();
	 }


}
