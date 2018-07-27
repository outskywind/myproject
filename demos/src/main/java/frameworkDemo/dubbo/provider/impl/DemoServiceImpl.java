package frameworkDemo.dubbo.provider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import frameworkDemo.dubbo.api.DemoService;

import java.util.Date;

/**
 * Created by quanchengyun on 2018/7/10.
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class DemoServiceImpl implements DemoService {


    @Override
    public String sayHello(String args) {
        System.out.println("recieved : " + args);
        return "returned " + args;
    }

    @Override
    public int[] sayHello(String hello, Date time) {
        return new int[0];
    }
}
