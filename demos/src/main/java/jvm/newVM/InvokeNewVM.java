package jvm.newVM;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by quanchengyun on 2018/3/7.
 */
public class InvokeNewVM {


    @Test
    public void test(){
        try {
            Process process = Runtime.getRuntime().exec("java -jar ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
