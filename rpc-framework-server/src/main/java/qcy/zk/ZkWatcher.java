package qcy.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class ZkWatcher  implements Watcher{

    @Override
    public void process(WatchedEvent event) {
	System.out.println("===event===recieved");
    }

}
