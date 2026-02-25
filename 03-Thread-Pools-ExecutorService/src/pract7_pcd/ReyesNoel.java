package pract7_pcd;

import java.util.Random;

public class ReyesNoel {

    //posicion 0 -> Melchor, posicion 1 -> Gaspar y posicion 2 -> Baltasar
    private boolean[] reyOcupado;
    private String[] nombreReyes;
    private boolean noelOcupado = false;
    private final Random random = new Random();

    public ReyesNoel() {
        reyOcupado = new boolean[3];
        nombreReyes = new String[3];

        for (int i = 0; i < 3; i++) {
            reyOcupado[i] = false;
        }
        noelOcupado = false;

        nombreReyes[0] = "Melchor";
        nombreReyes[1] = "Gaspar";
        nombreReyes[2] = "Baltasar";

    }

    public synchronized String atenderReyes(int id) throws InterruptedException {
        while (reyOcupado[0] && reyOcupado[1] && reyOcupado[2]) {
            System.out.println("Nino " + id + " esperando - TODOS los reyes ocupados");
            wait();
        }

        int pos = random.nextInt(3);
        int reyAsignado = -1;

        // Buscar rey disponible empezando por posición aleatoria
        for (int i = 0; i < 3; i++) {
            reyAsignado = (pos + i) % 3;
            if (!reyOcupado[reyAsignado]) {
                reyOcupado[reyAsignado] = true;
                break;
            }
        }

        System.out.println("Nino " + id + " atendido por " + nombreReyes[reyAsignado]);
        mostrarEstadoReyes();
        return nombreReyes[reyAsignado];
    }

    public synchronized void liberarReyes(String rey) {
        switch (rey) {
            case "Melchor":
                reyOcupado[0] = false;
                System.out.println("Melchor liberado");
                break;
            case "Gaspar":
                reyOcupado[1] = false;
                System.out.println("Gaspar liberado");
                break;
            case "Baltasar":
                reyOcupado[2] = false;
                System.out.println("Baltasar liberado");
                break;
        }
        mostrarEstadoReyes();
        notifyAll();
    }

    public synchronized void atenderNoel(int id) throws InterruptedException {
        while (noelOcupado && reyOcupado[0]) {
            System.out.println("Nino " + id + " esperando - Papa Noel y Melchor ocupados");
            wait();
        }

        if (!noelOcupado) {
            noelOcupado = true;
            System.out.println("Nino " + id + " atendido por Papa Noel");
        } else if (!reyOcupado[0]) {
            reyOcupado[0] = true;
            System.out.println("Nino " + id + " atendido por Melchor (como Papa Noel)");
        }

        mostrarEstadoReyes();
    }

    public synchronized void liberarNoel(int id) {
        if (noelOcupado) {
            noelOcupado = false;
            System.out.println("Papa Noel liberado por nino " + id);
        } else if (reyOcupado[0]) {
            // Verificamos que Melchor estaba actuando como Noel
            reyOcupado[0] = false;
            System.out.println("Melchor (como Noel) liberado por nino " + id);
        }
        mostrarEstadoReyes();
        notifyAll();
    }

    private void mostrarEstadoReyes() {
        System.out.println("ESTADO: Melchor=" + (reyOcupado[0] ? "OCUPADO" : "LIBRE")
                + ", Gaspar=" + (reyOcupado[1] ? "OCUPADO" : "LIBRE")
                + ", Baltasar=" + (reyOcupado[2] ? "OCUPADO" : "LIBRE")
                + ", Noel=" + (noelOcupado ? "OCUPADO" : "LIBRE"));
        System.out.println("");
    }

}
