package org.lotus.spring.service;

public class AppServiceImpl implements Appservice {
    @Override
    public void start() {
        System.out.println("------start()------");
    }

    @Override
    public void start(int mode) {
        System.out.println("------start(int mode)------");
    }

    @Override
    public void noIntercept() {
        System.out.println("------noIntercept()------");
    }
}
