package javaasist;

import org.junit.Test;

/**
 * Created by quanchengyun on 2019/7/17.
 */
public class ProxyTest {


    @Test
    public  void  test(){
        ProxyFactory proxyFactory =  new ProxyFactory();

        A a_1=  new A("just do it!");

        proxyFactory.getProxy(A.class,a_1);

    }
}
