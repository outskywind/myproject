package Network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

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

	// 正确的IP拿法，即优先拿site-local地址
	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			// 遍历所有的网络接口
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// 在所有的接口下再遍历IP
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
						if (inetAddr.isSiteLocalAddress()) {
							// 如果是site-local地址，就是它了
							return inetAddr;
						} else if (candidateAddress == null) {
							// site-local类型的地址未被发现，先记录候选地址
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress;
			}
			// 如果没有发现 non-loopback地址.只能用最次选的方案
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		} catch (Exception e) {
			UnknownHostException unknownHostException = new UnknownHostException(
					"Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}

}
