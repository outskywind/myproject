package org.lotus.boot;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class BootStrapLoader {


    static String LIBS_RESOURCE_PATH="classpath*:libs/**.jar";

    static String LIBS_RESOURCE_PATH_PREFIX="classpath*:";

    static String JAR_PROTOCAL="jar";

    //这个应该是在meta-inf 里指定的 yes
    static String SPRING_APP = "qcy.rpc.spring.BootStrap";

    static String SYS_TMP_DIR = "java.io.tmpdir";

    // 不能依赖任何第三方jar包
    public static void main(String[] args) {
        try {
            /**
             * https://docs.oracle.com/javase/8/docs/technotes/guides/lang/resources.html
             *
             * The methods in ClassLoader use the given String as the name of the resource without applying any absolute/relative transformation
             * (see the methods in Class). The name should not have a leading "/".
             *
             * 此外 以 / 结尾的都将当做目录对待，否则都将当做jar文件对待
             */
            //让system class loader 获取资源，用自定义的类加载器加载应用，如果没有父类加载器
            //就是继承自Ext class loader
            ClassLoader appLoader = Thread.currentThread().getContextClassLoader();
            // 使用的是jar包classPath相对路径
            String directory = LIBS_RESOURCE_PATH.substring(LIBS_RESOURCE_PATH_PREFIX.length(),LIBS_RESOURCE_PATH.lastIndexOf("/")+1);
            Enumeration<URL> resources = BootStrapLoader.class.getClassLoader().getResources(directory);
            Set<String> classpathResources  = null;
            Set<URL> jarFileUrls = null;
            while(resources.hasMoreElements()){
                URL resource = resources.nextElement();
                //System.out.println("jarURL="+resource);
                if(JAR_PROTOCAL.equals(resource.getProtocol())){
                    classpathResources =  JarResourceLoader.getClasspathResourceUrls(resource,".jar");
                    //classpathResources = new LinkedHashSet<String>();
                }
            }

            // Classloader 加载资源时新建了URL ，因此指定的URL已经不会被使用了,
            // 只能用URL.setURLStreamHandlerFactory()全局覆盖
            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory(appLoader));
            Set<URL> classLoadURL  =new LinkedHashSet<URL>(16);
            //System.out.println("java.io.tmpdir="+ System.getProperty(SYS_TMP_DIR));
            for(String url:classpathResources){
                //spec 如果是jar: 一定要 !/ 结尾
                //普通的spec 如果不以/结尾，那么也会被当做jar URL 加载
                //那么这个 URL 输入流就会被当做 Jar的输入流加载，然后解压到临时目录解析
                URL base = new URL("injar:"+url);
                classLoadURL.add(base);
            }
            URLClassLoader loader = new URLClassLoader(classLoadURL.toArray(new URL[classLoadURL.size()]), null,new URLStreamHandlerFactory(appLoader));
            // 设置到当前上下文加载器
            Thread.currentThread().setContextClassLoader(loader);
            // start the real main Method
            // System.out.println(ClassLoader.getSystemClassLoader());
            Class<?> mainClazz = Class.forName(SPRING_APP, true, loader);
            Method mainMethod = mainClazz.getMethod("main", new Class[] {args.getClass()});
            //JVM 解析会把数组拆成一个个对象
            mainMethod.invoke(null, new Object[] {args});
        } catch (Exception e) {
            e.printStackTrace();
        }









    }

}
