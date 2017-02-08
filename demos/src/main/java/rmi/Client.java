package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
	
	public static void main(String[] args){
		try {
			Iservice service = (Iservice) Naming.lookup("rmi://127.0.0.1:1099/service");
			
			System.out.println( service.getClass());
			
			
			System.out.println( service.getClass().getInterfaces()[0]);
			String result=service.getResult();
			System.out.println(result);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
