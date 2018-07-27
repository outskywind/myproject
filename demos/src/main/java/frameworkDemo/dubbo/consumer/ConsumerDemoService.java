package frameworkDemo.dubbo.consumer;

import frameworkDemo.dubbo.api.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by quanchengyun on 2018/7/10.
 */
@Service
public class ConsumerDemoService {

    @Autowired
    DemoService demoService;


    public void testDemo(){
        demoService.sayHello("hello at " + new Date());
    }

}
