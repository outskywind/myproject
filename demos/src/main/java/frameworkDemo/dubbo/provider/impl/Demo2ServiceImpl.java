package frameworkDemo.dubbo.provider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import frameworkDemo.dubbo.api.Demo2Service;

/**
 * Created by quanchengyun on 2018/7/17.
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class Demo2ServiceImpl implements Demo2Service {
    @Override
    public String sayHello2(String hello) {
        return null;
    }
}
