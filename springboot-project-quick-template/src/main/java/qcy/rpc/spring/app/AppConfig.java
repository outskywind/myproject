package qcy.rpc.spring.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import qcy.rpc.spring.app.service.Appservice;

/**
 * Created by quanchengyun on 2018/3/20.
 */
@Configuration
public class AppConfig {

    @Bean(initMethod = "start")
    public Appservice appservice(){
        return new Appservice();
    }


}
