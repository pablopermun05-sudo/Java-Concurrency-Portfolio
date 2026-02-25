package pkg;

import java.util.concurrent.Semaphore;
import static java.lang.Thread.sleep;
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

    public static void main(String args[]) throws InterruptedException {
        Random r = new Random();
        int random, tiempo, max = 20;

        Generador g = new Generador();
        g.setSize(1000, 525);
        CanvasCooperativa c = new CanvasCooperativa(1000, 500);
        g.add(c);
        g.setVisible(true);

        g.add(c);
        
        Semaphore semaforoPreadmision = new Semaphore(1);
        Semaphore semaforoDescarga = new Semaphore(1);
        Semaphore semaforoDocumentacion = new Semaphore(1);
        
        Thread[] h = new Thread[max];

        try {
            System.out.println("EMPIEZA EL PROGRAMA");
            for (int i = 0; i < max; i++) {
                tiempo = r.nextInt(2) + 1;
                random = r.nextInt(100);
                sleep(tiempo * 1000);

                if (random < 30) {
                    h[i] = new Thread(new Tractor(i+1, semaforoPreadmision, semaforoDescarga, semaforoDocumentacion, c));
                } else {
                    h[i] = new Camion(i+1, semaforoPreadmision, semaforoDescarga, semaforoDocumentacion, c);
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
