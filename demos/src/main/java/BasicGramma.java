
public class BasicGramma {

    public static void main(String[] args) {
        int i = "aaa".length();
        char ch = 17;
        int j = 012;
        System.out.println(j);
        int l = 023;

        int k = 0x78;
        short s = 1;
        // s=s+1;//java 会自动向长字节的类型扩展转型 +号会把整形转换为int,而 += 会强制转换结果
        short s2 = 1;
        s2 += 1;// +=会强制转换
        
        short aa = 1;
        short bb = Short.MAX_VALUE;
        //Short short1 = 32767;
        aa+=100000000;
        aa =(short) (aa + 100000000);
        
        short cc = aa;



        System.out.print(-(Long.MIN_VALUE+1)-1);

    }

}
