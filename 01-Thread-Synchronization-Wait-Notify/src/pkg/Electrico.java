/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg;

import java.util.Random;

/**
 *
 * @author Pablo Pérez Muñoz
 */
public class Electrico extends Thread{
    private final Parking p;
    private final int id;

    public Electrico(Parking p, int id) {
        this.p = p;
        this.id = id;
    }

    @Override
    public void run() {
        Random r = new Random();
        int random;
        try {
            random = r.nextInt(10) + 10;
            p.entraElectrico(id);
            
            //aparca
            Thread.sleep(random*1000);
            p.saleElectrico(id);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
