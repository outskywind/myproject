package org.lotus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.UrlResource;

import java.net.URL;
import java.util.Enumeration;

/**
 * Created by quanchengyun on 2018/3/20.
 */
@SpringBootApplication
public class BootStrap {

    public static void main(String[] args){

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.sleep(20000);
            System.out.println("the spring contextClassLoader="+cl);
            Enumeration<URL> urls = cl.getResources("META-INF/spring.factories");
            //真正检索resource是在hasMoreElements() 触发的
            while(urls.hasMoreElements()){
                URL u = urls.nextElement();
                System.out.println("spring.factories find= "+u);
                UrlResource ur= new UrlResource(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(BootStrap.class);
    }
}
