package pract7_pcd;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JOptionPane;

public class Generador {

    public static void main(String args[]) throws InterruptedException {
        Random r = new Random();
        int random, max = 50;

        ExecutorService thpNoel = Executors.newFixedThreadPool(7);
        ExecutorService thpReyes = Executors.newFixedThreadPool(7);
        ReyesNoel recurso = new ReyesNoel();

        ArrayList<Future<Integer>> resultados = new ArrayList<Future<Integer>>();
        ArrayList<Future> listaReyes = new ArrayList<Future>();
        
        System.out.println("INICIANDO SIMULACION NAVIDENA");
        System.out.println("Total de ninos: " + max);
        System.out.println("Thread Pool Reyes: 7 hilos");
        System.out.println("Thread Pool Noel: 7 hilos");
        System.out.println("=====================================");
        
        for (int i = 0; i < max; i++) {
            random = r.nextInt(100);

            if (random < 60) { // 60% Reyes, 40% Noel
                Future f = thpReyes.submit(new TipoReyes(i+1, recurso));
                listaReyes.add(f);
                System.out.println("Nuevo nino " + (i+1) + " quiere ver a los REYES MAGOS");
            } else {
                Future<Integer> f = thpNoel.submit(new TipoNoel(i+1, recurso));
                resultados.add(f);
                System.out.println("Nuevo nino " + (i+1) + " quiere ver a PAPA NOEL");
            }

            // Tiempo entre llegadas de 0.5 segundos
            Thread.sleep(500);
        }
        
        // Esperar a que terminen todos los hilos de Reyes
        for(int i = 0; i < listaReyes.size(); i++) {
            try {
                listaReyes.get(i).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Calcular tiempo total de Noel
        int total = 0;
        for(int i = 0; i < resultados.size(); i++) {
            try {
                total += resultados.get(i).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Cerrar los Thread Pools
        thpReyes.shutdown();
        thpNoel.shutdown();
        
        System.out.println("=====================================");
        System.out.println("SIMULACION TERMINADA");
        System.out.println("Tiempo total de Papa Noel: " + total + " segundos");
        System.out.println("Ninos atendidos por Reyes: " + listaReyes.size());
        System.out.println("Ninos atendidos por Noel: " + resultados.size());
        
        Thread.sleep(1000);
        JOptionPane.showMessageDialog(null, 
            "Simulacion terminada\n" +
            "Tiempo total de Papa Noel: " + total + " segundos\n" +
            "Ninos atendidos por Reyes: " + listaReyes.size() + "\n" +
            "Ninos atendidos por Noel: " + resultados.size(),
            "Resultados de la Simulacion",
            JOptionPane.INFORMATION_MESSAGE);
        
        System.exit(0);
    }
}