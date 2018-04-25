package org.lotus.boot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class ClassPathURLConnection extends java.net.URLConnection {

    //Logger log = LoggerFactory.getLogger(ClassPathURLConnection.class);

    private ClassLoader classLoader;

    public ClassPathURLConnection(URL url, ClassLoader classLoader)
    {
      super(url);
      this.classLoader = classLoader;
    }

    @Override
    public void connect() throws IOException {}

    @Override
    public InputStream getInputStream() throws IOException {
        String file = URLDecoder.decode(this.url.getFile(), "UTF-8");
        System.out.println("URLConnection getInputStream() file=" + file);
        /*if(file.equals("netty-all-4.1.6.Final.jar") || file.equals("libs/netty-all-4.1.6.Final.jar")){
            StackTraceElement[] stacks= Thread.currentThread().getStackTrace();
            for(StackTraceElement stack : stacks){
                System.out.println(stack);
            }
        }*/
        InputStream result = this.classLoader.getResourceAsStream(file);
        if (result == null) {
            System.out.println("file "+ file +" not found");
            throw new MalformedURLException("Could not open InputStream for URL '" + this.url + "'");
        }
        return result;
    }

}
