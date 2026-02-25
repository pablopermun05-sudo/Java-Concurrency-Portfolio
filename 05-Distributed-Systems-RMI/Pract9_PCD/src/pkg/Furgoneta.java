package pkg;

import remota.ParkingServidor;
import java.util.Random;

public class Furgoneta extends Thread{
    private final ParkingServidor p;
    private final int id;

    public Furgoneta(ParkingServidor p, int id) {
        this.p = p;
        this.id = id;
    }

    @Override
    public void run() {
        Random r = new Random();
        int random;
        try {
            random = r.nextInt(10) + 10;
            p.entraFurgoneta(id, false);
            
            //aparca
            Thread.sleep(random*1000);
            
            p.saleFurgoneta(id);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
