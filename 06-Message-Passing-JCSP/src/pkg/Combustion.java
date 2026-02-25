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
public class Combustion implements Runnable{
    private final int id;
    private final SharedChannelOutput entraC, saleC;
    private final AltingChannelInput permiso;

    public Combustion (int id, SharedChannelOutput entraC, AltingChannelInput permiso, SharedChannelOutput saleC) {
        this.id = id;
        this.entraC = entraC;
        this.permiso = permiso;
        this.saleC = saleC;
    }

    @Override
    public void run() {
        Random r = new Random();
        int random;
        try {
            random = r.nextInt(10) + 10;
            entraC.write(id);
            System.out.println("Espera combustion " + id);
            permiso.read();
            System.out.println("Combustion " + id + " entra");
            
            //aparca
            Thread.sleep(random*1000);
            
            saleC.write(id);
            System.out.println("Combustion " + id + " sale");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
