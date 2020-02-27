package org.lotus.template.dubbo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProviderBootStrap {


    public static  void main(String[] args){

        SpringApplication application = new SpringApplication(ProviderBootStrap.class);
        application.run(args);
        //向jvm 注册一个shutdown hook,以便优雅停止服务
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //... do some thing
        }));
    }
}
