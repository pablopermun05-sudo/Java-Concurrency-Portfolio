/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannel;

/**
 *
 * @author Pablo Pérez Muñoz
 */
public class Controlador extends Thread {

    private final AltingChannelInput entraE, entraC, entraF, saleC, sale, salir;
    private final One2OneChannel[] permiso;
    private int libre = 6;
    private int combustion = 0;

    public Controlador(AltingChannelInput entraC, AltingChannelInput entraE, AltingChannelInput EntraF, AltingChannelInput saleC, AltingChannelInput sale, One2OneChannel[] permiso, AltingChannelInput salir) {

        this.entraE = entraE;
        this.entraC = entraC;
        this.entraF = EntraF;
        this.saleC = saleC;
        this.sale = sale;
        this.permiso = permiso;
        this.salir = salir;
    }
    
    public void run() {
        Guard [] or = new Guard[6];
        boolean [] when = new boolean[6];
        int id;
        
        or[0] = entraC;
        or[1] = entraE;
        or[2] = entraF;
        or[3] = saleC;
        or[4] = sale;
        or[5] = salir;
        Alternative select = new Alternative(or);
        
        boolean fin = false;
        while(!fin) {
            when[0] = (entraE.pending() && combustion<2 && libre>0 && !entraF.pending()) || (!entraE.pending() && !entraF.pending() &&libre>0);
            when[1] = ((libre>0) && !entraF.pending());
            when[2] = (libre>0);
            when[3] = true;
            when[4] = true;
            when[5] = true;
            
            int cual = select.select(when);
            //int cual = select.priselect(when);
            
            switch(cual){
                case 0:
                    id = (int) entraC.read();
                    libre--;
                    permiso[id].out().write(id);
                    combustion++;
                    break;
                case 1:
                    id = (int) entraE.read();
                    libre--;
                    permiso[id].out().write(id);
                    break;
                case 2:
                    id = (int) entraF.read();
                    libre--;
                    permiso[id].out().write(id);
                    break;
                case 3:
                    id = (int) saleC.read();
                    libre++;
                    combustion--;
                    break;
                case 4:
                    id = (int) sale.read();
                    libre++;
                    break;
                case 5:
                    fin = true;
                    break;
                default:
                    System.out.println("Error en el select");
            }
        }
    }
}
