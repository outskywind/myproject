package nio;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.junit.Test;

public class SelectorTest {
	
	@Test
	public void test(){
		try {
			Selector selector = Selector.open();
			
			selector.select();
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	class Worker implements Runnable{
		
		private SocketChannel channel;
		
		public Worker(SocketChannel channel){
			this.channel=channel;
		}

		@Override
		public void run() {
			
			
		}
		
		
	}
	

}
