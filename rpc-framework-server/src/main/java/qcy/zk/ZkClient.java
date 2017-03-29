package qcy.zk;

import java.io.IOException;

import org.apache.zookeeper.ZooKeeper;

public class ZkClient {
    
    //连接
    private ZooKeeper zk ;
    
    private String zkHosts = "192.168.1.102:2181;192.168.1.103:2181;192.168.1.104:2181";
	
	/**
	 * 创建连接
	 * @throws IOException 
	 */
	public  void   getConnect() throws IOException{
	    zk = new ZooKeeper(zkHosts, 3000, new ZkWatcher());
	}

}
