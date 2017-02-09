package jvm;

public class TestDataTypes {
	
	byte b=-128;
	byte d=0;
	byte c=(byte)(b^d);
	byte arg = 1;
	char ch = 1;
	short sh =1;

	int[] bit = new int[8];
	
	public static void main(String[] args) {
		
		
		TestDataTypes td= new TestDataTypes();
		int key=td.b;
		for(int i=0;i<8;i++) {
			//java byte,char ,short 移位,异或会先转换成int
			td.bit[8-i-1]=key&1;
			key=key>>>1;
		}
		for(int i=0;i<td.bit.length;i++) {
			System.out.print(td.bit[i]);
		}
	}
	
	
}
