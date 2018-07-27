package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class NioServerDemo {

	private NioLoop loop;
	private AtomicBoolean started= new AtomicBoolean(false);
	public NioServerDemo(NioLoop loop){
		this.loop= loop;
	}
	
	public void bind(int port){
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();

			ssc.configureBlocking(false);

			ssc.socket().bind(new InetSocketAddress(9999));
			//4k
			ByteBuffer rev = ByteBuffer.allocateDirect(1024*4);
			//selector
			Selector sel = Selector.open();
			//注册通道
			ssc.register(sel, SelectionKey.OP_ACCEPT);
			//NioLoop mainReactor = new NioLoop("mianReactor");
			//轮询是必须的，保证进程一直运行状态
			if(started.compareAndSet(false,true)){loop.execute(
						() -> {
							runServer(sel);
						}
				);
				}
			} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public  void  runServer(Selector sel){
		try {
			//管理多通道
			while(true){
				//阻塞直到有可用的连接准备好
				//Epoll 空轮询bug on linux2.6-
				int n = sel.select();
				if(n==0){
					continue;
				}
				//获取准备就绪的key集合
				Iterator it=sel.selectedKeys().iterator();
				//
				while(it.hasNext()){
					SelectionKey key =(SelectionKey) it.next();
					//删除从已选择键集合，因为Selector的select() 方法只会将已准备的keys
					//加入这个集合，而不会删除之前的集合，至于为什么会这样JDK的实现
					it.remove();
					//如果是可接受状态
					if(key.isAcceptable()){
						ServerSocketChannel sscChannel  = (ServerSocketChannel)key.channel();
						SocketChannel sc=sscChannel.accept();
						
						sc.configureBlocking(false);
						//一个选择器，注册多个通道
						sc.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
					}
					//接着检查，如果是数据传输
				 	if(key.isReadable()){
						SocketChannel sc=(SocketChannel)key.channel();
						//数据处理，assume blcked a long time ;
						//for example , a complex remote call。
						processSocket(sc);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 处理连接的数据提交请求
	 * 对于每个连接的Socketchannel，是非阻塞读取的，
	 * 如果网络比较慢，网络连接建立之后，到接收到下次数据包之间会有比较长的时间
	 * 如果是阻塞的话，时间比较久，线程阻塞的也会很高
	 * 如果是NIO非阻塞的话，一个线程维护多个通道即可，但是如果请求的处理过程也会阻塞怎么办？
	 * 这才是真正问题的所在，请求过程也阻塞的话，比如分布式环境中，那么单线程的阻塞问题还是无法解决
	 * 反而加重了。所以每个请求一个线程真正的价值是解决分布式环境中阻塞问题。
	 *解决分布式环境中的问题：需要整个请求处理过程也是非阻塞的
	 *也就是说即使[servlet本身是阻塞的][servlet 本身阻塞之后，如果servlet是单线程处理的话，
	 *也就是这个channel阻塞了，这样问题还是没有解决，那么应该在阻塞的环节上改为NIO，
	 *那么其实还是需要改造servlet本身的，需要将所有会阻塞的远程调用改为NIO模式的，
	 *那么这个servlet是需要重写的，还是比较麻烦啊]，但是servlet容器的调度应该是非阻塞的
	 *这样就比较好解决：so,请求接受之后，在容器层实现一个NIO模型，
	 *将servlet抽象成底层的IO即可。这才是NIO的威力所在啊；
	 *需要一个选择器，来选择已经处理完毕返回数据的servlet，将servlet包装成一个channel，key就是对应的请求
	 *已选择的key就是已经处理完毕的，
	 *实现软件模拟的NIO
	 * @param sc
	 * @return
	 */
	private int processSocket(SocketChannel sc){
		
		ByteBuffer rev=ByteBuffer.allocate(4*1024);
		//
		try {
			while(sc.read(rev)!=-1){
				//do nothing
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	

}
