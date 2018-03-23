package org.qcy.boot;


public class URLStreamHandlerFactory implements java.net.URLStreamHandlerFactory {

    private ClassLoader classLoader;
    private URLStreamHandlerFactory chainFac;

    public URLStreamHandlerFactory(ClassLoader cl)
    {
      this.classLoader = cl;
    }

    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("injar".equals(protocol)) {
            return new URLStreamHandler(this.classLoader);
        }
        if (this.chainFac != null) {
            return this.chainFac.createURLStreamHandler(protocol);
        }
        return null;
    }

    public void setURLStreamHandlerFactory(URLStreamHandlerFactory fac) {
        this.chainFac = fac;
    }
}
