package pkg;

import java.util.concurrent.Semaphore;
import java.util.Random;

public class Camion extends Thread {
    private final int id;
    private final Semaphore semaforoPreadmision;
    private final Semaphore semaforoDescarga;
    private final Semaphore semaforoDocumentacion;
    private final CanvasCooperativa canvas;
    
    public Camion(int id, Semaphore semaforoPreadmision, Semaphore semaforoDescarga, Semaphore semaforoDocumentacion, CanvasCooperativa canvas) {
        this.id = id;
        this.semaforoPreadmision = semaforoPreadmision;
        this.semaforoDescarga = semaforoDescarga;
        this.semaforoDocumentacion = semaforoDocumentacion;
        this.canvas = canvas;
    }
    
    @Override
    public void run() {
        Random random = new Random();
        try {
            System.out.println("Camión " + id + " en cola");
            canvas.pintaColaVehiculo(id, 'C');
            
            // Fase 1: Preadmisión
            semaforoPreadmision.acquire();
            System.out.println("Camión " + id + " entra en PREADMISIÓN (Ocupa P).");
            canvas.pintaEntraVehiculo(id, 'C', 0);
            
            // Tiempo aleatorio entre 2-3 segundos
            Thread.sleep(random.nextInt(1000) + 2000);
            
            // Fase 2: Descarga
            semaforoDescarga.acquire();
            System.out.println("Camión " + id + " entra en DESCARGA (Ocupa DES).");
            canvas.pintaEntraVehiculo(id, 'C', 1);
            semaforoPreadmision.release();
            
            // Tiempo aleatorio entre 2-3 segundos
            Thread.sleep(random.nextInt(1000) + 2000);
            
            // Fase 3: Documentación
            semaforoDocumentacion.acquire();
            System.out.println("Camión " + id + " entra en DOCUMENTACION (Ocupa DOC).");
            canvas.pintaEntraVehiculo(id, 'C', 2);
            semaforoDescarga.release();
            
            // Tiempo aleatorio entre 2-3 segundos
            Thread.sleep(random.nextInt(1000) + 2000);
            
            // Finalización
            canvas.pintaSaleVehiculo(id);
            semaforoDocumentacion.release();
            
        } catch (InterruptedException e) {
            System.out.println("Camión " + id + " interrumpido");
            Thread.currentThread().interrupt();
        }
    }
    
    public int getCamionId() {
        return id;
    }
}
