package frameworkDemo.dubbo.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by quanchengyun on 2018/7/10.
 */
@SpringBootApplication
public class DubboProviderDemoMain {
    static Logger log  = LoggerFactory.getLogger(DubboProviderDemoMain.class);

    public static void main(String[] args) {
        //ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-provider.xml"});
        //context.start();
        try{
            SpringApplication.run(DubboProviderDemoMain.class,args);
            System.in.read(); // press any key to exit
        }catch (Exception e){
            log.error("异常：",e);
        }

    }



}
