/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkg;

/**
 *
 * @author Pablo Pérez Muñoz
 */
public class Parking {

    private int libres = 6, comb, elecEspera;
    private final CanvasParking canvas;

    public Parking(CanvasParking c) {
        this.canvas = c;
    }

    public boolean parkingLleno() {
        return libres == 0;
    }

    public synchronized void entraCombustion(int id) throws Exception {
        boolean haEntrado = false;
        
        while (parkingLleno() || (elecEspera > 0 && comb > 1)) {
            if (!haEntrado) {
                System.out.println("Combustion: " + id + " espera (parking lleno)");
                canvas.pintaColaCombustion(id);
                haEntrado = true;
            }
            wait();
        }

        libres--;
        comb++;
        System.out.println("Combustion: " + id + " entra en el parking");
        canvas.pintaEntraCombustion(id);
    }

    public synchronized void saleCombustion(int id) throws Exception {
        comb--;
        libres++;
        canvas.pintaSaleCoche(id);
        System.out.println("Combustion: " + id + " sale");
        notifyAll();
    }

    public synchronized void entraElectrico(int id) throws Exception {
        boolean haEntrado = false;
        elecEspera++;
        while (parkingLleno()) {
            if (!haEntrado) {
                System.out.println("Electrico: " + id + " espera (parking lleno)");
                canvas.pintaColaElectrico(id);
                haEntrado = true;
            }
            wait();
        }

        elecEspera--;
        libres--;
        System.out.println("Electrico: " + id + " entra en el parking");
        canvas.pintaEntraElectrico(id);
    }

    public synchronized void saleElectrico(int id) throws Exception {
        libres++;
        canvas.pintaSaleCoche(id);
        System.out.println("Electrico: " + id + " sale");
        notifyAll();
    }
}
