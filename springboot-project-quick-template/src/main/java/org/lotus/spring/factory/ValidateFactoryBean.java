package org.lotus.spring.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by quanchengyun on 2018/4/28.
 */
public class ValidateFactoryBean<T>  implements FactoryBean<T>,InitializingBean {

    private T target;

    public void setTarget(T target) {
        this.target = target;
    }

    @Override
    public T getObject() throws Exception {
        return null;
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
