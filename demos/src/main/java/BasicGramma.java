
public class BasicGramma {

    public static void main(String[] args) {
        int i = "aaa".length();
        char ch = 17;
        int j = 012;
        System.out.println(j);
        int l = 023;

        int k = 0x78;
        short s = 1;
        // s=s+1;//java 会自动向长字节的类型扩展转型
        short s2 = 1;
        s2 += 1;// += 会强制类型转换

    }

}
