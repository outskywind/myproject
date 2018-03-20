package org.qcy.boot;

import java.io.File;
import java.io.InputStream;
import java.net.*;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * Created by quanchengyun on 2018/3/20.
 */
public class BootStrapClassLoader extends URLClassLoader {


    public BootStrapClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public BootStrapClassLoader(URL[] urls) {
        super(urls);
    }

    public BootStrapClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }


    @Override
    protected Class<?> findClass(final String name)
            throws ClassNotFoundException
    {
        final Class<?> result;
        try {
            result = AccessController.doPrivileged(
                    new PrivilegedExceptionAction<Class<?>>() {
                        public Class<?> run() throws Exception {
                            String path = name.replace('.', '/').concat(".class");
                            //Resource res = ucp.getResource(path, false);
                            for(URL jarsInJar: getURLs()){
                                System.out.println(jarsInJar);
                                URLConnection con = jarsInJar.openConnection();
                                if (con instanceof JarURLConnection) {
                                    // Should usually be the case for traditional JAR files.
                                    JarURLConnection jarCon = (JarURLConnection) con;
                                    //JarFile jarFile = jarCon.getJarFile();
                                    //System.out.println(jarFile.getName());
                                    //JarEntry jarEntry = jarCon.getJarEntry();
                                    JarInputStream input = (JarInputStream)jarCon.getInputStream();
                                    //input.

                                    //JarFile jarFiles = new JarFile(new File(""));
                                    //System.out.println(jarEntry != null ? jarEntry.getName() : "");
                                    //Enumeration<JarEntry> entries = jarFile.entries();
                                    //while(entries.hasMoreElements()) {
                                    //    JarEntry entry = entries.nextElement();
                                    //    System.out.println("---"+(entry != null ? entry.getName() : ""));
                                    //}
                                    //jarCon.getContent();
                                    //rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
                                    //closeJarFile = !jarCon.getUseCaches();
                                }
                            }


                            /*if (res != null) {
                                try {
                                    return defineClass(name, res);
                                } catch (IOException e) {
                                    throw new ClassNotFoundException(name, e);
                                }
                            } else {
                                return null;
                            }*/
                            return null;
                        }
                    }, null);
        } catch (java.security.PrivilegedActionException pae) {
            throw (ClassNotFoundException) pae.getException();
        }
        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        return result;
    }










}
