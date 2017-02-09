package Network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class TestInetAddress {
	private static String HOST ="www.pingan.com";
	//private Log logger = LogFactory.getLog(this.getClass());
	private String[] ips = null;
	@Test
	public void testGetAllByName() {
		TestInetAddress tAddress =new TestInetAddress();
		ips = tAddress.getAllByName();
		System.out.println("returned result="+ "");
		printAddress();
		System.out.println(this.getClass().getClassLoader().getResource(""));
	}
	
	
	private void printAddress() {
		for(int i=0;i<ips.length;i++) {
			System.out.println(ips[i]);
		}
	}
	/**
	 * all the parameters are the reference of the original variable
	 * in java ,"=" means that change the variable's reference  to the new one;
	 * @param ips is not the same with the original variable ips.
	 */
	public String[] getAllByName() {
		boolean isExecuted=false;
		String[] _ips =null;
		try {
			//有防火墙会抛出异常
			InetAddress[] DNSIps = InetAddress.getAllByName(HOST);
			isExecuted=true;
			_ips = new String[DNSIps.length];
			for(int i=0;i<DNSIps.length;i++) {
				_ips[i] = DNSIps[i].getHostAddress();
			}
			//ips here is the reference of the "original ips".
			//ips=_ips;
			//now ips changed to the reference of the _ips.
			return _ips;
		} catch (UnknownHostException e) {
			//logger.info(e);
			if(isExecuted) {
				System.out.println("方法中抛出异常，异常后的语句也会执行");
			}
			System.out.println("方法中抛出异常，则会执行catch块，然后执行finally块，然后结束方法返回");
			return _ips;
		}
	}
}
