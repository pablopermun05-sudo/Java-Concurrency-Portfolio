package pkg;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.ArrayList;


public class CanvasParking extends Canvas {

    // Clase interna que representa un coche o plaza libre
    class Elemento {

        int id;
        char tipo; // 'E' = eléctrico, 'C' = combustión, 'L' = libre

        private Elemento(int id, char tipo) {
            this.id = id;
            this.tipo = tipo;
        }

        public int getId() {
            return id;
        }

        public char getTipo() {
            return tipo;
        }

        @Override
        public boolean equals(Object obj) { //sirve para el contains de las colas
            if (this == obj) 
                return true;
            else if (obj == null || getClass() != obj.getClass()) 
                return false;
            
            Elemento e = (Elemento) obj;
            return this.id == e.id && this.tipo == e.tipo;
        }

    }
    
    private final Image imgCombustion, imgElectrico, imgFondo;

    // Colas para representar el estado visual
    private final ArrayList<Elemento> colaElectrico = new ArrayList<>();
    private final ArrayList<Elemento> colaCombustion = new ArrayList<>();
    private final ArrayList<Elemento> colaParking = new ArrayList<>();

    // Constantes de dibujo
    private final int anchoCochesParking = 115;
    private final int anchoCochesCola = 50;
    private final int altoCochesParking = 200;
    private final int altoCochesCola = 90;
    private final int espacio = 60;
    private final int numPlazas = 6;

    // Posiciones en pantalla
    private final int[] poscolaElectrico = {50, 60, 60};
    private final int[] poscolaCombustion = {50, 160, 60};

    // Constructor
    public CanvasParking(int ancho, int alto) throws InterruptedException {
        super.setSize(ancho, alto);
        super.setBackground(Color.white);

        // Carga de imágenes
        imgFondo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/fondo.png"));
        imgCombustion = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/combustion.png"));
        imgElectrico = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/electrico.png"));

        // Espera a que las imágenes se carguen
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(imgFondo, 0);
        tracker.addImage(imgCombustion, 1);
        tracker.addImage(imgElectrico, 2);
        tracker.waitForAll();

        // Inicializa el parking con plazas libres ('L')
        for (int i = 0; i < 6; i++) {
            colaParking.add(new Elemento(-1, 'L')); // 'L' = Libre
        }
    }

    //---------------------------------------------------------
    // Dibujo
    //---------------------------------------------------------
    @Override
    public synchronized void update(Graphics g) {
        paint(g);
    }

    @Override
    public synchronized void paint(Graphics g) {

        g.drawImage(imgFondo, 0, 0, null);
        Font f1 = new Font("Rockwell", Font.BOLD, 20);
        g.setFont(f1);

        // Cola eléctricos
        g.setColor(Color.white);
        g.drawString("Cola Eléctricos", poscolaElectrico[0], poscolaElectrico[1] - 10);
        g.setColor(Color.gray);
        g.fillRect(poscolaElectrico[0], poscolaElectrico[1], 700 - poscolaElectrico[0] - 10, poscolaElectrico[2]);
        for (int i = 0; i < colaElectrico.size(); i++) {
            Elemento e = colaElectrico.get(i);
            g.drawImage(imgElectrico, poscolaElectrico[0] + espacio * i, poscolaElectrico[1] - 15, anchoCochesCola, altoCochesCola, this);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(e.getId()), poscolaElectrico[0] + 25 + espacio * i, poscolaElectrico[1] + 25);
        }

        // Cola combustión
        g.setColor(Color.white);
        g.drawString("Cola Combustión", poscolaCombustion[0], poscolaCombustion[1] - 10);
        g.setColor(Color.gray);
        g.fillRect(poscolaCombustion[0], poscolaCombustion[1], 700 - poscolaCombustion[0] - 10, poscolaCombustion[2]);
        for (int i = 0; i < colaCombustion.size(); i++) {
            Elemento e = colaCombustion.get(i);
            g.drawImage(imgCombustion, poscolaCombustion[0] + espacio * i, poscolaCombustion[1] - 15, anchoCochesCola, altoCochesCola, this);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(e.getId()), poscolaCombustion[0] + 25 + espacio * i, poscolaCombustion[1] + 25);
        }

        // Parking
        g.setColor(Color.white);
        g.drawString("Parking (6 plazas)", 50, 265);

        for (int i = 0; i < numPlazas; i++) {
            int x = 43 + i * 100;  // ajustamos los coches en el parking
            int y = 275;
            Elemento e = colaParking.get(i);

            // Fondo plaza
            g.setColor(Color.white);
            g.drawRect(50 + i * 100, 275, 100, 200);

            // Dibujo del coche si hay
            if (e.getTipo() == 'L') {
                g.setColor(Color.WHITE);
                g.drawString("Libre", 75 + 100 * i, 365);

            } else {
                Image img;
                if(e.getTipo() == 'C')
                    img = imgCombustion;
                else
                    img = imgElectrico;
                
                g.drawImage(img, x, y, anchoCochesParking, altoCochesParking, this);
            }
        }
    }

    public synchronized void pintaColaCombustion(int id) {
        colaCombustion.add(new Elemento(id, 'C'));

        repaint();
    }

    public synchronized void pintaEntraCombustion(int id) {
        int i = 0;
        boolean encontrado = false;
        Elemento e = new Elemento(id, 'C');

        if (colaCombustion.contains(e)) {
            while (!encontrado) {
                if (colaCombustion.get(i).getId() == id) {
                    encontrado = true;
                } else {
                    i++;
                }
            }
            colaCombustion.remove(i);
        }

        i = 0;
        encontrado = false;
        while (!encontrado) {
            if (colaParking.get(i).getTipo() == 'L') {
                colaParking.set(i, e);
                encontrado = true;
            } else {
                i++;
            }
        }

        repaint();
    }

    public synchronized void pintaColaElectrico(int id) {
        colaElectrico.add(new Elemento(id, 'E'));

        repaint();
    }

    public synchronized void pintaEntraElectrico(int id) {
        int i = 0;
        boolean encontrado = false;
        Elemento e = new Elemento(id, 'E');

        if (colaElectrico.contains(e)) {
            while (!encontrado) {
                if (colaElectrico.get(i).getId() == id) {
                    encontrado = true;
                } else {
                    i++;
                }
            }
            colaElectrico.remove(i);
        }

        i = 0;
        encontrado = false;
        while (!encontrado) {
            if (colaParking.get(i).getTipo() == 'L') {
                colaParking.set(i, e);
                encontrado = true;
            } else {
                i++;
            }
        }

        repaint();
    }

    public synchronized void pintaSaleCoche(int id) {
        int i = 0;
        Elemento e = new Elemento(-1, 'L');
        boolean encontrado = false;
        while (!encontrado) {
            if (colaParking.get(i).getId() == id) {
                colaParking.set(i, e);
                encontrado = true;
            } else {
                i++;
            }
        }

        repaint();
    }

}
