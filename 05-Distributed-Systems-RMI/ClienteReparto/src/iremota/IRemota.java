package iremota;

import java.rmi.Remote;
import java.rmi.RemoteException;

//IRemota hereda de Remote
public interface IRemota extends Remote {
    
    int incrementar() throws RemoteException;
    
    void entraFurgoneta(int id, boolean boton) throws InterruptedException, RemoteException;
    
    void saleFurgoneta(int id) throws InterruptedException, RemoteException;
    
}