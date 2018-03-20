package programme;

import org.junit.Test;

/**
 * Created by quanchengyun on 2018/3/6.
 */
public class StringTests {


    @Test
    public void test(){
        String str = "file:/opt/server/sevend/invitecenter-consumer/invitecenter-consumer-1.0-SNAPSHOT.jar!/BOOT-INF/lib/flume-logs-1.0.0-SNAPSHOT.jar!/";

        str =  str.substring(0,str.indexOf(".jar"));
        str = str.substring(str.lastIndexOf("/")+1);
        str = str.substring(0,str.lastIndexOf("-"));
        str = str.substring(0,str.lastIndexOf("-"));
        System.out.println(str);
    }
}
