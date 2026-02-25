package pract7_pcd;

import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TipoReyes implements Runnable {
    private final int id;
    private final ReyesNoel recurso;
    private String reyAsignado;
    
    public TipoReyes(int id, ReyesNoel recurso){
        this.id = id;
        this.recurso = recurso;
    }
    
    @Override
    public void run() {
        Random random = new Random();
        try {
            
            // Determinamos qué rey nos atendió
            reyAsignado = recurso.atenderReyes(id);
            
            int tiempoAtencion = random.nextInt(3) + 3; // 3-5 segundos
            System.out.println("Nino " + id + " con " + reyAsignado + " por " + tiempoAtencion + " segundos");
            sleep(tiempoAtencion * 1000);
            
            System.out.println("Nino " + id + " termino con " + reyAsignado);
            recurso.liberarReyes(reyAsignado);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(TipoReyes.class.getName()).log(Level.SEVERE, null, ex);
        }             
    }
}