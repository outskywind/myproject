package programme;
import org.junit.Test;


public class ObjectAndInteger {

    private static final String  V ="1";

    @Test
    public void  test(){

        Integer v1 = 1;

        Integer v2 = 1;

        System.out.println(v1==v2);

        String str1 = "1"; // = 号是直接引用

        String str2 = "1";

        String  str3 = new String("1");
        System.out.println(str1==str2);
        System.out.println(str1==str3);

        String str4 = V; // = 号是直接引用

        String  str5 = V;
        System.out.println(str4==str5); // == 号比较的就是值，而引用类型的值是被引用的地址，所有常量都是在老年代中定义的唯一地址

        AClass a = new AClass();
        BClass b = new BClass();

        System.out.println(a.value==b.value);

    }




}
