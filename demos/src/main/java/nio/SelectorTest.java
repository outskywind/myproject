package nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class SelectorTest {
	
	@Test
	public void test(){
		try {
			//Selector selector = Selector.open();
			//execute(new Reactor(selector));
			Thread.sleep(2000);
			//now we register the channel
			//ServerSocketChannel serverSocketChannel =  ServerSocketChannel.open();
			//serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
			int port = 6767;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//
	void bind(int port, Reactor[] bosses , Worker[] workers){

	}


	public void execute(Runnable task){
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(task);
	}

	class ReactorGroup{
		private Reactor[] reactors;
		public ReactorGroup(int nReactors) throws Exception{
			reactors = new Reactor[nReactors<=0?1:nReactors];
			for(int i= 0; i<reactors.length;i++){
				//Selector selector = Selector.open();
				reactors[i] = new Reactor();
			}
		}
		//

	}

	class Reactor implements Runnable{

		private Selector selector;
		private WorkerGroup workers;
		ExecutorService executor = Executors.newSingleThreadExecutor();

		public void start(int port) throws Exception{
			selector = Selector.open();
			executor.execute(this);
			Thread.sleep(2000);
			//after selector started loop, then we register the channel
			ServerSocketChannel serverSocketChannel =  ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(port));
			serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

		}

		@Override
		public void run() {
			try{
				while(true){
					long start = System.currentTimeMillis();
					//select 是阻塞操作，因此如果没有绑定 channel
					// 或者没有请求进来，都会导致一直阻塞
					selector.select(100);

					Set<SelectionKey> selected = selector.selectedKeys();
					Iterator<SelectionKey> it = selected.iterator();
					while(it.hasNext()){
						it.remove();
						SelectionKey  key = it.next();
						try{
							if(key.isAcceptable()){
								System.out.println("accepted");
								ServerSocketChannel serverSocketChannel  = (ServerSocketChannel)key.channel();
								SocketChannel socketChannel = serverSocketChannel.accept();

							}
							if(key.isConnectable()){
								System.out.println("connected");
							}
						}catch (Exception e){
							e.printStackTrace();
						}
					}
					long cur = System.currentTimeMillis();
					if(cur-start<100){
						Thread.sleep(100-(cur-start));
					}
				}

			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}



	class WorkerGroup{
		private Worker[] workers ;

		public WorkerGroup(int worker){
			workers = new Worker[worker<=1?1:worker];
		}

		public void dispatch(SocketChannel channel){
			//when boss group is single thread . there is no worries about concurrent
			//
		}
	}

	class Worker{
		private ExecutorService executor ;
		private Selector selector;
		public Worker(int nthreads) throws Exception{
			executor = Executors.newFixedThreadPool(nthreads, r -> new Thread(new ThreadGroup("worker-group"),r));
			selector = Selector.open();
		}

		public void accept(SocketChannel channel)throws Exception{
			if(!channel.isOpen()){
				return;
			}
			channel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}

	}


	

}
