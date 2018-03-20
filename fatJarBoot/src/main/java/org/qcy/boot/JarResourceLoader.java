package org.qcy.boot;


import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by quanchengyun on 2018/3/20.
 */
public class JarResourceLoader {

    /**
     * 对于 jar in jar 来说，只能获取到 jarEntry ，所有不同的 jarEntry 的 JarFile 是同一个外部执行命令的classpath 中的jar
     * 只能获取到 jar in jar 的字节流
     *
     * @param jarURL
     * @param subPattern
     * @return
     * @throws Exception
     */
    public static Set<URL> getResouceUrls(URL jarURL, String subPattern) throws Exception{


        Set<URL> result = new LinkedHashSet<URL>(16);
        URLConnection con = jarURL.openConnection();
        JarFile jarFile = null;

        String rootEntryPath = null;
        boolean closeJarFile=false;

        if (con instanceof JarURLConnection) {
            // Should usually be the case for traditional JAR files.
            JarURLConnection jarCon = (JarURLConnection) con;
            jarFile = jarCon.getJarFile();
            JarEntry jarEntry = jarCon.getJarEntry();
            rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
            closeJarFile = !jarCon.getUseCaches();
        }

        try {
            if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
                // Root entry path must end with slash to allow for proper matching.
                // The Sun JRE does not return a slash here, but BEA JRockit does.
                rootEntryPath = rootEntryPath + "/";
            }
            for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
                JarEntry entry = entries.nextElement();
                String entryPath = entry.getName();
                //这是啥意思？本来就肯定是root开头的吧
                if (entryPath.startsWith(rootEntryPath)) {
                    String relativePath = entryPath.substring(rootEntryPath.length());
                    result.add(new URL(jarURL,relativePath));
                }
            }
        }
        finally {
            if (closeJarFile) {
                jarFile.close();
            }
        }
        return result;
    }


}
