package pract7_pcd;

import static java.lang.Thread.sleep;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

public class TipoNoel implements Callable<Integer> {
    private final int id;
    private final ReyesNoel recurso;
    
    public TipoNoel(int id, ReyesNoel recurso){
        this.id = id;
        this.recurso = recurso;
    }
    
    @Override
    public Integer call() {
        Random random = new Random();
        try {
            recurso.atenderNoel(id);
            
            int tiempoAtencion = random.nextInt(3) + 3; // 3-5 segundos
            System.out.println("Nino " + id + " con Papa Noel/Melchor por " + tiempoAtencion + " segundos");
            sleep(tiempoAtencion * 1000);
            
            System.out.println("Nino " + id + " termino con Papa Noel/Melchor");
            recurso.liberarNoel(id);
            
            return tiempoAtencion;
            
        } catch (InterruptedException ex) {
            Logger.getLogger(TipoNoel.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}