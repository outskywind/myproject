package org.lotus.spring.app;

import org.lotus.spring.aop.Pointcut;
import org.lotus.spring.factory.ValidateFactoryBean;
import org.lotus.spring.service.AppServiceImpl;
import org.lotus.spring.service.Appservice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by quanchengyun on 2018/3/20.
 */
@Configuration
public class AppConfig {

    @Bean
    public Appservice appservice(){
        return new AppServiceImpl();
    }


    @Bean("appServiceAop")
    @Pointcut({"start(int mode)"})
    //that is ok , we cant use  method parameter type inject here
    public ValidateFactoryBean factory(){
        ValidateFactoryBean<Appservice> appserviceFatory = new ValidateFactoryBean<>();
        appserviceFatory.setTarget(Appservice.class,appservice());
        return appserviceFatory;
    }

    /* this will cause the problem ,maybe the factory referenced itself ,cause this factorybean to init failed.
    public ValidateFactoryBean factory(Appservice appservice){
        ValidateFactoryBean<Appservice> appserviceFatory = new ValidateFactoryBean<>();
        appserviceFatory.setTarget(appservice());
        return appserviceFatory;
    }
    */

}
