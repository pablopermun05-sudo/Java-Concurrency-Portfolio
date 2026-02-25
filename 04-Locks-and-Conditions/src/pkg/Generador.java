package pkg;

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

    public static void main(String[] args) throws InterruptedException {
        Generador g = new Generador();
        g.setSize(1344, 838);
        CanvasPasarela canvas = new CanvasPasarela(1344, 838);
        g.add(canvas);
        g.setVisible(true);

        int max = 30, random, tiempo;

        Pasarela pasarela = new Pasarela(canvas);
        Thread[] h = new Thread[max];
        Random r = new Random();

        try {
            // Debe lanzar 30 hilos
            for (int i = 0; i < max; i++) {
                Thread t;
                random = r.nextInt(100);
                // 75% Derecha, 25% Izquierda
                if (random < 75) {
                    // Derecha hereda de Thread
                    h[i] = new Derecha(pasarela, i + 1);
                } else {
                    // Izquierda implementa Runnable, se pasa al constructor de Thread
                    h[i] = new Thread(new Izquierda(pasarela, i + 1));
                }

                h[i].start();

                tiempo = r.nextInt(2) + 1;
                sleep(tiempo * 1000);
            }

            // Esperar a que finalicen todos los hilos
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
