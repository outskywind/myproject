package org.bootStrap;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class URLTest {

    @Test
    public void test() {
        try {


            URL url = new URL("jar:classpath:rpc-framework-server-0.0.1-SNAPSHOT.jar!/ ");

            URLClassLoader ucl = new URLClassLoader(new URL[] {url});
            ucl.findResource("");
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }

    }

}
