package collections;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class HashMapTest {



    @Test
    public void testMap() {
        Map<String, String> t = new HashMap<String, String>();
        t.put("1", "2");

        Map<Integer, Integer> map = new HashMap<>();
        Integer v1 = 1;

        Integer v2 = new Integer(1);

        map.put(v1,5);

        System.out.println(map.get(v2));



    }

}
