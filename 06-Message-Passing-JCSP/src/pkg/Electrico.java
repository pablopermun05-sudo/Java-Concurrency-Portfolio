/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg;

import java.util.Random;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.SharedChannelOutput;

/**
 *
 * @author Pablo Pérez Muñoz
 */
public class Electrico extends Thread{
    private final int id;
    private final SharedChannelOutput entraE, sale;
    private final AltingChannelInput permiso;

    public Electrico(int id, SharedChannelOutput entraE, AltingChannelInput permiso, SharedChannelOutput sale) {
        this.id = id;
        this.entraE = entraE;
        this.permiso = permiso;
        this.sale = sale;
    }

    @Override
    public void run() {
        Random r = new Random();
        int random;
        try {
            random = r.nextInt(10) + 10;
            entraE.write(id);
            System.out.println("Espera electrico " + id);
            permiso.read();
            System.out.println("Electrico " + id + " entra");
            
            //aparca
            Thread.sleep(random*1000);
            
            sale.write(id);
            System.out.println("Electrico " + id + " sale");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
