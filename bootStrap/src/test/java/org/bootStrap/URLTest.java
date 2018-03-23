package org.bootStrap;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.qcy.boot.URLStreamHandlerFactory;

public class URLTest {

    @Test
    public void test() {
        try {

            URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory(this.getClass().getClassLoader()));
            URL url = new URL("jar:injar:rpc-framework-server-0.0.1-SNAPSHOT.jar!/ ");
            System.out.println("init file="+url.getFile());

            //URLClassLoader ucl = new URLClassLoader(new URL[] {url});
            //ucl.findResource("");
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

    }

}
