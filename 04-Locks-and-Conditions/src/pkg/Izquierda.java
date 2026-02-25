package pkg;

import java.util.Random;

public class Izquierda implements Runnable { // Implementa Runnable 
    private Pasarela pasarela;
    private int id;

    public Izquierda(Pasarela pasarela, int id) {
        this.pasarela = pasarela;
        this.id = id;
    }

    @Override
    public void run() {
        Random r = new Random();
        int tiempo;
        try {
            pasarela.entraIzquierda(id);
            
            // Tiempo de paso aleatorio entre 4 y 6 segundos
            tiempo = r.nextInt(3) + 4;
            Thread.sleep(tiempo * 1000);
            pasarela.saleIzquierda(id);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
