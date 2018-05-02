package org.lotus.spring.service;

/**
 * Created by quanchengyun on 2018/3/20.
 */
public class Appservice {
    
    public void start(){
        System.out.println("Appservice started, no intercept..");
    }


    public void start(int mode){
        System.out.println("start method override ,intercept me..");
    }

    public void noIntercept(){
        System.out.println("do not Intercept me !!");
    }
}
