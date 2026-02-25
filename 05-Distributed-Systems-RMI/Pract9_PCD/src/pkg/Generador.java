package pkg;

import remota.ParkingServidor;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;



public class Generador extends javax.swing.JFrame {

    public Generador() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) throws InterruptedException, RemoteException {
        Random r = new Random();
        int random, tiempo, max = 30;

        Generador g = new Generador();
        g.setSize(715, 650);
        CanvasParking c = new CanvasParking(715, 625);
        g.add(c);
        g.setVisible(true);

        ParkingServidor p = new ParkingServidor(c); // pasar canvas al parking
        g.add(c);
        
        Thread[] h = new Thread[max];
        
        //Ahora mismo ya he creado el puerto, cuando meta la información el puerto será 7 (LocateRegistry)
        Registry reg = LocateRegistry.createRegistry(7);
        
        //Utilizo reg.rebind, si el nombre existe lo machaco, en el nombre "remota", le podría haber puesto lo que yo quisiera.
        reg.rebind("parking", p); //El objeto clase es 'p' (La clase remota)

        try {
            System.out.println("EMPIEZA EL PROGRAMA");
            for (int i = 0; i < max; i++) {
                tiempo = r.nextInt(2) + 1;
                random = r.nextInt(100);
                sleep(tiempo * 1000);

                if (random < 50) {
                    h[i] = new Electrico(p, i+1);
                } else if (random < 90) {
                    h[i] = new Thread(new Combustion(p, i+1));
                } else {
                    h[i] = new Furgoneta(p, i+1);
                }

                h[i].start();
            }

            System.out.println("TODOS LOS HILOS CREADOS");

            for (int i = 0; i < max; i++) {
                h[i].join();
            }

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("FIN DEL PROGRAMA");

        g.dispose();

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
