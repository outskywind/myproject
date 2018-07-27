package nio;

/**
 * Created by quanchengyun on 2018/7/25.
 */
public class NioServerMain {

    public static void main(String[] args){

        NioServerDemo server = new NioServerDemo(new NioLoop("mainReactor"));
        server.bind(7654);
    }
}
