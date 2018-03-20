package org.qcy.boot;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Created by quanchengyun on 2018/3/19.
 */
public class BootLuancher {


    static String LIBS_RESOURCE_PATH="classpath*:libs/**.jar";

    static String LIBS_RESOURCE_PATH_PREFIX="classpath*:";

    static String JAR_PROTOCAL="jar";

    public static  void main(String[] args){
        try{
            //ClassLoader 的 getResource 不能使用/开头 ,而 Class 中的 getResource 用 /表示绝对路径，当然是classpath下的
            //
            System.out.println(BootLuancher.class.getClassLoader().getResource("org/qcy/boot/BootLuancher.class"));
            /**
             * https://docs.oracle.com/javase/8/docs/technotes/guides/lang/resources.html
             *
             * The methods in ClassLoader use the given String as the name of the resource without applying any absolute/relative transformation
             * (see the methods in Class). The name should not have a leading "/".
             *
             * 以 / 结尾的都将当做目录对待，否则都将当做jar文件对待
             */
            //yes
            String directory = LIBS_RESOURCE_PATH.substring(LIBS_RESOURCE_PATH_PREFIX.length(),LIBS_RESOURCE_PATH.lastIndexOf("/")+1);
            System.out.println(directory);
            URL resource0 =
                    BootLuancher.class.getClassLoader().getResource("libs/spring-core-4.3.8.RELEASE.jar");
            System.out.println(resource0);
            URL resource1 =
                    BootLuancher.class.getClassLoader().getResource("libs/spring-core-4.3.8.RELEASE.jar/META-INF/license.txt");
            System.out.println(resource1);

            Enumeration<URL> resources = BootLuancher.class.getClassLoader().getResources(directory);
            Set<URL> results  =new LinkedHashSet<URL>(16);
            while(resources.hasMoreElements()){
                URL resource = resources.nextElement();
                //
                if(JAR_PROTOCAL.equals(resource.getProtocol())){
                    results =  JarResourceLoader.getResouceUrls(resource,".jar");
                }
            }
            //we have some mistake ,because  the  systemClassLoader loaded all the  main project classes .
            //so of cause  all the dependencies will first be asked to load by the systemClassloader , so the systemClassloader must be able
            // to delegate to our customized classloader. this is because of the parent delegate model of JVM
            //ClassLoader system = ClassLoader.getSystemClassLoader();
            // 因为你无法更改系统类加载器和它的机制。因此你只能告诉JVM 启动之后要用自定义的类加载器加载全部的应用代码，
            // 而你的自定义类加载器必须将系统类加载器设置为父加载器
            //直接将jar!里面的jar作为URL传递进去，默认的findClass是无法加载到这个资源的,需要自定义findClass
            URLClassLoader contextClassLoader = new BootStrapClassLoader(results.toArray(new URL[0]),ClassLoader.getSystemClassLoader());
            Thread.currentThread().setContextClassLoader(contextClassLoader);
            //Class starter =  Class.forName("org.qcy.boot.starter.SpringBootApplicationStarter");
            //Class starter =  Class.forName("org.springframework.boot.SpringApplication");
            Class starter = contextClassLoader.loadClass("org.springframework.boot.SpringApplication");
            Method main = starter.getDeclaredMethod("main", String[].class);
            //JVM 解析会把数组拆成一个个对象
            main.invoke(null,(Object)args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
