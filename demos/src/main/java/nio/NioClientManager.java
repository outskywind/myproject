package nio;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quanchengyun on 2018/7/25.
 */
public class NioClientManager {

    Map<String,NioClient> clients=new HashMap<>();

    // protocol://server:port/resource_path
    public Object sendRequest(String protocol,String host ,String path, Object obj) {
        NioClient client = getClient(host);
        if(client==null){
            throw new IllegalStateException("can't get nio client");
        }
        Object result = client.sendRequest(obj);
        return result;
    }

    /**
     * NioClient 要处理连接断开的问题
     * @param host
     * @return
     */
    protected NioClient getClient(String host){
        try{
            NioClient client = clients.get(host);
            if(client==null){
                int idx = host.indexOf(":");
                int port = 80;
                if(idx>-1){
                    host = host.substring(0,idx+1);
                    port = Integer.parseInt(host.substring(idx,host.length()));
                }
                client = new NioClientDemo(host,port);
                clients.put(host,client);
            }
            if(!client.isActive()){
                client.connect();
            }
            return client;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
