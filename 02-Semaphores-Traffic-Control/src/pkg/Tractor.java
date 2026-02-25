package pkg;

import java.util.concurrent.Semaphore;
import java.util.Random;


public class Tractor implements Runnable {
    private int id;
    private Semaphore semaforoPreadmision;
    private Semaphore semaforoDescarga;
    private Semaphore semaforoDocumentacion;
    private CanvasCooperativa canvas;

    public Tractor(int id, Semaphore sPreadmision, Semaphore sDescarga, Semaphore sDocumentacion, CanvasCooperativa canvas) {
        this.id = id;
        this.semaforoPreadmision = sPreadmision;
        this.semaforoDescarga = sDescarga;
        this.semaforoDocumentacion = sDocumentacion;
        this.canvas = canvas;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            System.out.println("Tractor " + id + " en cola");
            canvas.pintaColaVehiculo(id, 'T');

            // Fase 1: Preadmisión
            semaforoPreadmision.acquire();
            System.out.println("Tractor " + id + " entra en PREADMISIÓN (Ocupa P).");
            canvas.pintaEntraVehiculo(id, 'T', 0);
            
            // Tiempo aleatorio entre 2-3 segundos
            Thread.sleep(random.nextInt(1000) + 2000);

            // Fase 2: Descarga
            semaforoDescarga.acquire();
            System.out.println("Tractor " + id + " entra en DESCARGA (Ocupa P y DES).");
            canvas.pintaEntraVehiculo(id, 'T', 1);
            
            // Tiempo aleatorio entre 2-3 segundos
            Thread.sleep(random.nextInt(1000) + 2000);

            // Fase 3: Documentación
            semaforoDocumentacion.acquire();
            System.out.println("Tractor " + id + " entra en DOCUMENTACION (Ocupa DES y DOC).");
            canvas.pintaEntraVehiculo(id, 'T', 2);
            semaforoPreadmision.release();
            
            
            // Tiempo aleatorio entre 2-3 segundos
            Thread.sleep(random.nextInt(1000) + 2000);

            // Finalización
            canvas.pintaSaleVehiculo(id);
            semaforoDescarga.release(); 
            semaforoDocumentacion.release(); 

        } catch (InterruptedException e) {
            System.out.println("Tractor " + id + " interrumpido.");
        }
    }
}
