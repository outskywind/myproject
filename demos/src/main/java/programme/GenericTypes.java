package programme;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

/**
 * Created by quanchengyun on 2018/5/3.
 */
public class GenericTypes {


    /**
     *  type parameter 指的是静态定义时的那个参数名字 也就是 "T" ,
     // parameterized type 指的是实例化时动态具体传入的类型
     */
    public void test() throws Exception{
        Method processMethod = this.getClass().getMethod("xxx");
        Parameter[] params = processMethod.getParameters();
        ParameterizedType pt = ((ParameterizedType)params[0].getParameterizedType());
        Class ptc;
        if(pt==null|| pt.getActualTypeArguments()==null || pt.getActualTypeArguments().length==0){
            ptc = String.class;
        }else {
            ptc = (Class)pt.getActualTypeArguments()[0];
        }
    }


    static class P<A,B> {

    }

    static class C  extends P<String,String> {

    }


    @Test
    public void testP(){
        P<String,String> v = new C();
    }



}
