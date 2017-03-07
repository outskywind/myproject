package collections;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListTests {

    @Test
    public void test() {

        List<String> orgList = new ArrayList<String>();

        orgList.add("1");
        orgList.add("2");
        orgList.add("3");
        orgList.add("4");

        List<String> subList = orgList.subList(0, 1);

        System.out.println("subList size=  " + subList.size());
        orgList.set(0, "new 1 set by orgList");
        // subList 返回的底层存储与原来的是相同的
        System.out.println(subList.get(0));

    }

}
