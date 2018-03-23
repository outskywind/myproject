package org.qcy.boot;

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
            //让system class loader 获取资源，用自定义的类加载器加载应用，如果没有父类加载器
            //就是继承自Ext class loader
            ClassLoader appLoader = Thread.currentThread().getContextClassLoader();
            // 使用的是jar包classPath相对路径
            //Manifest mf = new Manifest(appLoader.getResourceAsStream("META-INF/MANIFEST.MF"));
            //Attributes attr = mf.getMainAttributes();
            //String files = attr.getValue("Class-Path");
            //String[] cps = splitSpaces(files);
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
                //URL base2 =  new URL("jar:injar:"+url+"!/");
                //System.out.println("the init base2 url=" +base2+" & file="+base2.getFile());
                //URL class_ = new URL(base,SPRING_APP.replace('.', '/').concat(".class"));
                //URLClassPath ucp = new URLClassPath(new URL[]{base});
                //ucp.getResource(SPRING_APP.replace('.', '/').concat(".class"),false);
                //System.out.println("url.tostring()="+base);
                //URL jarURL = new URL("jar", "", -1, base + "!/", null);
                //URLConnection jarConn  = jarURL.openConnection();
                //JarFile jarFile = ((JarURLConnection)jarConn).getJarFile();
                //System.out.println(jarFile);
                //System.out.println(base.toString());
                classLoadURL.add(base);
            }
            URLClassLoader loader = new URLClassLoader(classLoadURL.toArray(new URL[classLoadURL.size()]), null,new URLStreamHandlerFactory(appLoader));
            //URLClassLoader loader = new URLClassLoader((new URL[]{new URL("jar:injar:libs/spring-boot-1.5.3.RELEASE.jar!/")}), null);
            // 设置到当前上下文加载器
            Thread.currentThread().setContextClassLoader(loader);
            // start the real main Method
            // String mainClass = attr.getValue("Main-Class");
            System.out.println(ClassLoader.getSystemClassLoader());
            Class<?> mainClazz = Class.forName(SPRING_APP, true, loader);
            // mainClazz.newInstance();
            Method mainMethod = mainClazz.getMethod("main", new Class[] {args.getClass()});
            mainMethod.invoke(null, new Object[] {args});
        } catch (Exception e) {
            e.printStackTrace();
        }









    }

}
