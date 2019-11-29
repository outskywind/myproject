package programme;

import org.junit.Test;

public class HiinterfaceTest {


    @Test
    public void test(){
        HiInterface hiInterface = () -> {
            System.out.println("another");
            return "null";
        };
        //默认方法的好处就是实现类可以直接使用而无需实现，减少了一点代码量
        hiInterface.hello();
        hiInterface.anotherMethod();
    }
}
