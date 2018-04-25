package org.lotus.boot;

import java.io.IOException;
import java.net.URL;


public class URLStreamHandler extends java.net.URLStreamHandler {

    private ClassLoader classLoader;

    public URLStreamHandler(ClassLoader classLoader)
    {
      this.classLoader = classLoader;
    }

    @Override
    protected ClassPathURLConnection openConnection(URL u) throws IOException {
        return new ClassPathURLConnection(u, this.classLoader);
    }

    @Override
    protected void parseURL(URL url, String spec, int start, int limit) {
        String file;
        // url = injar
        // spec = injar:spring-context-4.3.4.RELEASE.jar
        //System.out.println("url=" + url);
        //System.out.println("spec=" + spec);
        if (spec.startsWith("injar:")) {
            file = spec.substring(6);
        } else {
            if (url.getFile().equals("./")) {
                file = spec;
            } else {
                if (url.getFile().endsWith("/")) {
                    file = url.getFile() + spec;
                } else {
                    file = spec;
                }
            }
        }
        //System.out.println("parseURL file=" + file);
        setURL(url, "injar", "", -1, null, null, file, null, null);
    }

}
