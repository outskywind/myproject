package AOP;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.*;
import org.junit.Test;


import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * Created by quanchengyun on 2018/11/15.
 */
public class CGLibTest {



    @Test
    public void test()throws Exception{
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(OriginService.class);
        //CallbackFilter filter=new TracerCallbackFilter();
        //enhancer.setCallbackFilter(filter);
        enhancer.setCallback(new Tracer$nextContext());
        //DebuggingClassWriter classWriter =  new DebuggingClassWriter(2);
        //byte[] clazz =  enhancer.getStrategy().generate(enhancer);
        //new FileOutputStream("d:/CGLIB$$1.class").write(clazz);
        enhancer.create();
        byte[] clazz =  enhancer.getStrategy().generate(enhancer);
        new FileOutputStream("d:/CGLIB$$1.class").write(clazz);
    }


    public static  class Tracer$nextContext implements MethodInterceptor {

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            System.out.println("---before");
            Object result  =  methodProxy.invokeSuper(obj,args);
            System.out.println("---after");
            return result;
        }
    }




}
