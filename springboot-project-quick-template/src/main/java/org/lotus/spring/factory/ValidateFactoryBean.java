package org.lotus.spring.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Proxy;

/**
 * Created by quanchengyun on 2018/4/28.
 */
public class ValidateFactoryBean<T>  implements FactoryBean<T>,InitializingBean {

    private T target;
    private Class<T> type;

    public void setTarget(Class<T> type,T target) {
        this.target = target;
        this.type = type;
    }

    @Override
    public T getObject() {
        //反射调用要检查一堆条件，访问更多的对象，比直接调用慢
        T proxy = (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                new Class<?>[]{type}, (proxy1, method, args) -> {
                    System.out.println("----before----");
                    Object result =  method.invoke(proxy1,args);
                    System.out.println("----after----");
                    return result;
                });
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return target.getClass();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
