package Network;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class URLTest {


    @Test
    public void testURL(){
        try {
            new URL("http://service-c/getName").openConnection().connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
