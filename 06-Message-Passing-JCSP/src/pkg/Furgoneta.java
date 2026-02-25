package pkg;

import java.util.Random;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.SharedChannelOutput;

public class Furgoneta extends Thread{
    private final int id;
    private final SharedChannelOutput entraF, sale;
    private final AltingChannelInput permiso;

    public Furgoneta(int id, SharedChannelOutput entraF, AltingChannelInput permiso, SharedChannelOutput sale) {
        this.id = id;
        this.entraF = entraF;
        this.permiso = permiso;
        this.sale = sale;
    }

    @Override
    public void run() {
        Random r = new Random();
        int random;
        try {
            random = r.nextInt(10) + 10;
            entraF.write(id);
            System.out.println("Espera furgoneta " + id);
            permiso.read();
            System.out.println("Furgoneta " + id + " entra");
            
            //aparca
            Thread.sleep(random*1000);
            
            sale.write(id);
            System.out.println("Furgoneta " + id + " sale");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
