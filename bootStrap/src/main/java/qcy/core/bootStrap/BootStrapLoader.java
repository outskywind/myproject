package qcy.core.bootStrap;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class BootStrapLoader {

    private static String serverBootStrap = "qcy.rpc.server.BootStrapServer";

    // 不能依赖任何第三方jar包
    public static void main(String[] args) {
        try {
            ClassLoader appLoader = Thread.currentThread().getContextClassLoader();
            // 使用的是jar包classPath相对路径
            Manifest mf = new Manifest(appLoader.getResourceAsStream("META-INF/MANIFEST.MF"));
            Attributes attr = mf.getMainAttributes();
            String files = attr.getValue("Class-Path");
            String[] cps = splitSpaces(files);
            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory(appLoader));
            URL[] urls = new URL[cps.length + 1];
            // URL[] urls = new URL[] {"jar:file:"};
            urls[0] = new URL("injar:./");
            for (int i = 1; i < urls.length; i++) {

                urls[i] = new URL("jar:injar:" + cps[i - 1] + "!/");
            }

            URLClassLoader loader = new URLClassLoader(urls, null);
            // 设置到当前上下文加载器
            Thread.currentThread().setContextClassLoader(loader);
            // start the real main Method
            // String mainClass = attr.getValue("Main-Class");
            Class<?> mainClazz = Class.forName(serverBootStrap, true, loader);
            // mainClazz.newInstance();
            Method mainMethod = mainClazz.getMethod("main", new Class[] {args.getClass()});
            mainMethod.invoke(null, new Object[] {args});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String[] splitSpaces(String line) {
        if (line == null) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        int firstPos = 0;
        while (firstPos < line.length()) {
            int lastPos = line.indexOf(' ', firstPos);
            if (lastPos == -1) {
                lastPos = line.length();
            }
            if (lastPos > firstPos) {
                result.add(line.substring(firstPos, lastPos));
            }
            firstPos = lastPos + 1;
        }
        return result.toArray(new String[result.size()]);
    }
}
