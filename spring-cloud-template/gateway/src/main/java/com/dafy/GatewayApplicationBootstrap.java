package com.dafy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableConfigServer
public class GatewayApplicationBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplicationBootstrap.class, args);
    }

}
