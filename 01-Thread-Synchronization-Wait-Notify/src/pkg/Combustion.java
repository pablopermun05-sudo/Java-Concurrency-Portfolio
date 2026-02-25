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
public class Combustion implements Runnable{
    private final Parking p;
    private final int id;

    public Combustion (Parking p, int id) {
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
