package remota;

import iremota.IRemota;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import pkg.CanvasParking;



public class ParkingServidor extends UnicastRemoteObject implements IRemota {

    private int libres, comb, elecEspera, furgEspera;
    private int contador;
    private final CanvasParking canvas;

    public ParkingServidor(CanvasParking c) throws RemoteException {
        super();
        this.canvas = c;
        libres = 6;
        comb = 0;
        elecEspera = 0;
        furgEspera = 0;
        contador = 1;
    }

    public boolean parkingLleno() {
        return libres == 0;
    }

    public synchronized void entraCombustion(int id) throws InterruptedException {
        boolean haEntrado = false;
        
        while (parkingLleno() || (elecEspera > 0 && comb > 1) || furgEspera > 0) {
            if (!haEntrado) {
                System.out.println("Combustion: " + id + " espera (parking lleno)");
                canvas.pintaColaCombustion(id);
                haEntrado = true;
            }
            wait();
        }

        libres--;
        comb++;
        System.out.println("Combustion: " + id + " entra en el parking");
        canvas.pintaEntraCombustion(id);
    }

    public synchronized void saleCombustion(int id) throws InterruptedException {
        comb--;
        libres++;
        canvas.pintaSaleCoche(id);
        System.out.println("Combustion: " + id + " sale");
        notifyAll();
    }

    public synchronized void entraElectrico(int id) throws InterruptedException {
        boolean haEntrado = false;
        elecEspera++;
        while (parkingLleno() || furgEspera > 0) {
            if (!haEntrado) {
                System.out.println("Electrico: " + id + " espera (parking lleno)");
                canvas.pintaColaElectrico(id);
                haEntrado = true;
            }
            wait();
        }

        elecEspera--;
        libres--;
        System.out.println("Electrico: " + id + " entra en el parking");
        canvas.pintaEntraElectrico(id);
    }

    public synchronized void saleElectrico(int id) throws InterruptedException {
        libres++;
        canvas.pintaSaleCoche(id);
        System.out.println("Electrico: " + id + " sale");
        notifyAll();
    }

    @Override
    public synchronized void entraFurgoneta(int id, boolean boton) throws InterruptedException, RemoteException {
        boolean haEntrado = false;
        furgEspera++;
        while (parkingLleno()) {
            if (!haEntrado) {
                System.out.println("Furgoneta: " + id + " espera (parking lleno)");
                canvas.pintaColaFurgoneta(id, boton);
                haEntrado = true;
            }
            wait();
        }

        furgEspera--;
        libres--;
        System.out.println("Furgoneta: " + id + " entra en el parking");
        canvas.pintaEntraFurgoneta(id, boton);
    }

    @Override
    public synchronized void saleFurgoneta(int id) throws InterruptedException, RemoteException {
        libres++;
        canvas.pintaSaleCoche(id);
        System.out.println("Furgoneta: " + id + " sale");
        notifyAll();
    }
    
    @Override
    public int incrementar() throws RemoteException{
        return contador++;
    }
}
