package net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class testInaddress {
	
	@Test
	public void test() {
		
		String  host= "www.pingan.com";
		
		try {
			InetAddress[] adr = InetAddress.getAllByName(host);
			for(int i=0;i<adr.length;i++) {
				System.out.println(adr[i].getHostAddress());
			}
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
		
	}

}
