package javaasist;

import javassist.*;
import javassist.bytecode.AccessFlag;

/**
 * Created by quanchengyun on 2019/7/17.
 */
public class ProxyFactory {

    /**
     * 这种方式需要对每一个具体的类进行织入，就和
     * aspectj 的做法，静态织入一样
     * 而动态代理的
     * @param originalClazz
     * @param target
     * @param <T>
     */
    public <T> void getProxy(Class<T> originalClazz, T  target){
        ClassPool classPool  =  ClassPool.getDefault();
        try {
            CtClass original_cc = classPool.get(originalClazz.getName());
            CtClass proxy_cc = classPool.makeClass(originalClazz.getName()+"$$JavaassistBytecodeProxy");
            //设置为父类
            proxy_cc.setSuperclass(original_cc);
            //添加一个filed 原对象
            CtField target__ct = new CtField(original_cc,"target",proxy_cc);
            proxy_cc.addField(target__ct);
            //覆盖父类的所有public 方法
            for(CtMethod m_ct:original_cc.getDeclaredMethods()){
                if (AccessFlag.isPublic(m_ct.getModifiers())){
                    //CtMethod  new_m_ct = new CtMethod(m_ct.getMethodInfo(),proxy_cc);
                    proxy_cc.addMethod(m_ct);
                }
            }
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

}
