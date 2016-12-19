package qcy.core.bootStrap;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class URLConnection extends java.net.URLConnection {

    private ClassLoader classLoader;

    public URLConnection(URL url, ClassLoader classLoader)
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
        InputStream result = this.classLoader.getResourceAsStream(file);
        if (result == null) {
            throw new MalformedURLException("Could not open InputStream for URL '" + this.url + "'");
        }
        return result;
    }

}
