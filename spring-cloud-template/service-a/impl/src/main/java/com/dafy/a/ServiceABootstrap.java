package com.dafy.a;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages="com.dafy")
public class ServiceABootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ServiceABootstrap.class, args);
    }
}
