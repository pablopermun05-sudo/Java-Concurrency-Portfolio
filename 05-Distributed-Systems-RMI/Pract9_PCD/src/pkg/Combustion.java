package pkg;

import remota.ParkingServidor;
import java.util.Random;

public class Combustion implements Runnable{
    private final ParkingServidor p;
    private final int id;

    public Combustion (ParkingServidor p, int id) {
        this.p = p;
        this.id = id;
    }

    @Override
    public void run() {
        Random r = new Random();
        int random;
        try {
            random = r.nextInt(10) + 10;
            p.entraCombustion(id);
            
            //aparca
            Thread.sleep(random*1000);
            
            p.saleCombustion(id);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
