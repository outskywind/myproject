package concurrency.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class SocketBlockTest {
	
	@Test
	public void test(){
		
		//worker thead
		ExecutorService service = Executors.newCachedThreadPool();
		
		try {
			ServerSocket server = new ServerSocket(9999);
			
			while(true){
				//blocked here
				Socket socket = server.accept();
				
				//Byte[] socket.getInputStream();
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	class Worker implements Runnable{
		
		private String message;
		
		public void Worker(String message){
			this.message = message;
		}
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	

}
