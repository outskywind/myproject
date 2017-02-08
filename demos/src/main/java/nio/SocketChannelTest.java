package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelTest {
	
	
	public static void main(String[] args){
		
		
		//
		try {
			SocketChannel scc = SocketChannel.open();
			//非阻塞通道，连接建立过程非阻塞
			scc.configureBlocking(false);
			
			//指定连接目标主机的端口
			scc.connect(new InetSocketAddress(9999));
			//检测是否建立完成
			while(!scc.finishConnect()){
				//...
			}
			System.out.println("connection established");
			
			//是准备读取的状态
			ByteBuffer buffer = ByteBuffer.wrap("Hello Kitty".getBytes());
			
			scc.write(buffer);
			
			scc.close();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
