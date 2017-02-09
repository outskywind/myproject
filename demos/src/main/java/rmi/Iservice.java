package rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Iservice  extends Remote,Serializable{
	
	public String getResult() throws RemoteException;

}
