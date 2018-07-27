package frameworkDemo.dubbo.api;

import java.util.Date;

/**
 * Created by quanchengyun on 2018/7/10.
 */
public interface DemoService {

    String sayHello(String args);

    int[] sayHello(String hello, Date time);

}
