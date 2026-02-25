package pkg;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Pasarela {
    private final CanvasPasarela canvas;
    
    private static final int MAX_PERSONAS = 3;
    private int numDerecha = 0; // Personas cruzando hacia la derecha
    private int numIzquierda = 0; // Personas cruzando hacia la izquierda
    private int esperandoDerecha = 0;
    private int esperandoIzquierda = 0;

    // Mecanismos de Concurrencia 
    private final ReentrantLock mutex = new ReentrantLock();
    private final Condition colaDerecha = mutex.newCondition();
    private final Condition colaIzquierda = mutex.newCondition();
    
    public Pasarela(CanvasPasarela canvas){
        this.canvas = canvas;
    }

    public void entraIzquierda(int id) throws InterruptedException {
        mutex.lock();
        try {
            while (esperaIzquierda()) {
                System.out.println("Espera izquierda.   Ocupacion: I = " + numIzquierda + ", D = " + numDerecha + " - id = " + id);
                esperandoIzquierda++;
                canvas.pintaColaIzquierda(id);
                colaIzquierda.await();
                esperandoIzquierda--;
            }
            numIzquierda++;
            if (numDerecha == 2 && esperandoDerecha > 0) {
                colaDerecha.signal();
            }

            System.out.println("Entra izquierda.    Ocupacion: I = " + numIzquierda + ", D = " + numDerecha + " - id = " + id);
        
            canvas.pintaEntraIzquierda(id);
        } finally {
            mutex.unlock();
        }
    }

    public void entraDerecha(int id) throws InterruptedException {
        mutex.lock();
        try {
            while (esperaDerecha()) {
                System.out.println("Espera derecha.     Ocupacion: I = " + numIzquierda + ", D = " + numDerecha + " - id = " + id);
                esperandoDerecha++;
                canvas.pintaColaDerecha(id);
                colaDerecha.await();
                esperandoDerecha--;
            }
            numDerecha++;
            if (numIzquierda == 2 && esperandoIzquierda > 0) {
                colaIzquierda.signal();
            }

            System.out.println("Entra derecha.      Ocupacion: I = " + numIzquierda + ", D = " + numDerecha + " - id = " + id);

            canvas.pintaEntraDerecha(id);
        } finally {
            mutex.unlock();
        }
    }

    public void saleIzquierda(int id) {
        mutex.lock();
        try {
            numIzquierda--;
            System.out.println("Sale izquierda.     Ocupacion: I = " + numIzquierda + ", D = " + numDerecha + " - id = " + id);

            canvas.pintaSale(id);
            if (!esperaIzquierda() && esperandoIzquierda > 0) {
                colaIzquierda.signal();
            }

        } finally {
            mutex.unlock();
        }
    }

    public void saleDerecha(int id) {
        mutex.lock();
        try {
            numDerecha--;
            System.out.println("Sale derecha.       Ocupacion: I = " + numIzquierda + ", D = " + numDerecha + " - id = " + id);

            canvas.pintaSale(id);
            if (!esperaDerecha() && esperandoDerecha > 0) {
                colaDerecha.signal();
            }

        } finally {
            mutex.unlock();
        }
    }

    private boolean esperaIzquierda() {
        if (numIzquierda == MAX_PERSONAS) {
            return true;
        } else if (numDerecha == 0 && numIzquierda == 2) {
            return true;
        } else {
            return false;
        }
    }

    private boolean esperaDerecha() {
        if (numDerecha == MAX_PERSONAS) {
            return true;
        } else if (numIzquierda == 0 && numDerecha == 2) {
            return true;
        } else {
            return false;
        }
    }
}
