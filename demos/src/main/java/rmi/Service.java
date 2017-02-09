package rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import sun.rmi.registry.RegistryImpl_Skel;
import sun.rmi.registry.RegistryImpl_Stub;

public class Service  extends UnicastRemoteObject implements Iservice {

	protected Service() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getResult() throws RemoteException {
		// TODO Auto-generated method stub
		return "recieved";
	}
	
	public static void main(String[] args){
		try {
			Registry registry=LocateRegistry.createRegistry(1099);
			Iservice service= new Service();
			//Naming.bind("rmi://127.0.0.1:1099/service", service);
			//返回的是stub
			
			registry.bind("service", service);
			System.out.println(registry.getClass().getName());
			System.out.println(registry.lookup("service").getClass().getName());
			
			
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}  catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}

}
