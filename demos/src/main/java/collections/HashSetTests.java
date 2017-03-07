package collections;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

public class HashSetTests {

    @Test
    public void test() {
        Set<String> t = new HashSet<String>();

        t = null;

        Set<String> t2 = null;
        // t==null
        // System.out.println(t.containsAll(t2));


        Set<String> t3 = new LinkedHashSet<String>();
        t3.add("90后");
        t3.add("准新客");
        t3.add("铜牌会员");

        Set<String> t4 = new LinkedHashSet<String>();
        t4.add("铜牌会员");

        System.out.println(!t3.containsAll(t4));

    }

}
