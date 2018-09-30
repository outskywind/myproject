package programme;

import org.junit.Test;

/**
 * Created by quanchengyun on 2018/8/30.
 */
public class ReferrenceTest {


    class T1{
        long count = 1;
        boolean flag = false;

        String str ="ok";

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }

    @Test
    public void test(){

        T1 t = new T1();

        long cc = t.getCount();
        t.setCount(2);
        boolean f = t.isFlag();
        t.setFlag(true);

        String str = t.getStr();
        t.setStr("hello");
        System.out.println("cc="+cc+",f="+f+",str="+str);

    }
}
