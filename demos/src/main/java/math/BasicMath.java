package math;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by quanchengyun on 2018/5/9.
 */
public class BasicMath {

    public static void main(String[] args){
        System.out.println(1999/1000*1000);
        t();

    }


    public static void t(){
        List l  = new ArrayList<>();
        l.add(new Date());
        l.add(1);
        l.add("hello");
        System.out.println(l);
    }
}
