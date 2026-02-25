package pkg;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.ArrayList;

public class CanvasPasarela extends Canvas {

    class Persona {

        int id;
        char tipo; // 'I' = va hacia izquierda, 'D' = va hacia derecha, 'L' = libre

        private Persona(int id, char tipo) {
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
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Persona e = (Persona) obj;
            return this.id == e.id && this.tipo == e.tipo;
        }
    }

    private final Image imgDerecha, imgIzquierda, imgFondo;

    // colaDerecha: Gente esperando en el lado IZQUIERDO para cruzar hacia la DERECHA
    // colaIzquierda: Gente esperando en el lado DERECHO para cruzar hacia la IZQUIERDA
    private final ArrayList<Persona> colaIzquierda = new ArrayList<>();
    private final ArrayList<Persona> colaDerecha = new ArrayList<>();
    private final ArrayList<Persona> colaPasarela = new ArrayList<>();

    private final int anchoPersona = 30;
    private final int altoPersona = 60;
    private final int Y_PUENTE_BASE = 160;

    public CanvasPasarela(int ancho, int alto) throws InterruptedException {
        super.setSize(ancho, alto);
        super.setBackground(Color.white);

        imgFondo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/fondo.png"));
        imgIzquierda = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/izquierda.png"));
        imgDerecha = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/derecha.png"));

        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(imgFondo, 0);
        tracker.addImage(imgDerecha, 1);
        tracker.addImage(imgIzquierda, 2);
        tracker.waitForAll();

        // Inicializa la pasarela con 6 huecos libres
        for (int i = 0; i < 6; i++) {
            colaPasarela.add(new Persona(-1, 'L'));
        }
    }

    @Override
    public synchronized void update(Graphics g) {
        paint(g);
    }

    @Override
    public synchronized void paint(Graphics g) {
        g.drawImage(imgFondo, 0, 0, getWidth(), getHeight(), this);

        Font f1 = new Font("Arial", Font.BOLD, 16);
        g.setFont(f1);

        // 2. COLA DERECHA (Gente en la izquierda esperando ir a la derecha)
        g.setColor(Color.BLACK);
        int startX_EscaleraIzq = 130;
        int startY_EscaleraIzq = 170;

        g.drawString("Cola Derecha", 20, 250); // Texto informativo más abajo

        for (int i = 0; i < colaDerecha.size(); i++) {
            Persona e = colaDerecha.get(i);

            // Calculamos posición en diagonal descendente hacia la izquierda
            // Cuanto mayor es 'i' (más atrás en la cola), más abajo y a la izquierda están
            int x = startX_EscaleraIzq - (i * 25);
            int y = startY_EscaleraIzq + (i * 15);

            // Control simple para que no se salgan demasiado de la pantalla si la cola es gigante
            if (x < 10) {
                x = 10;
            }
            if (y > getHeight() - altoPersona) {
                y = getHeight() - altoPersona;
            }

            g.drawImage(imgDerecha, x, y, anchoPersona, altoPersona, this);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(e.getId()), x + 5, y - 5);
        }

        // 3. COLA IZQUIERDA (Gente en la derecha esperando ir a la izquierda)
        g.setColor(Color.BLACK);
        int startX_EscaleraDer = getWidth() - 160;
        int startY_EscaleraDer = 170;

        g.drawString("Cola Izquierda", getWidth() - 150, 250);

        for (int i = 0; i < colaIzquierda.size(); i++) {
            Persona e = colaIzquierda.get(i);

            // Calculamos posición en diagonal descendente hacia la derecha
            int x = startX_EscaleraDer + (i * 25);
            int y = startY_EscaleraDer + (i * 15);

            // Control simple de bordes
            if (x > getWidth() - anchoPersona) {
                x = getWidth() - anchoPersona;
            }
            if (y > getHeight() - altoPersona) {
                y = getHeight() - altoPersona;
            }

            g.drawImage(imgIzquierda, x, y, anchoPersona, altoPersona, this);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(e.getId()), x + 5, y - 5);
        }

        // 4. PERSONAS EN LA PASARELA
        int margenPuente = 300;
        int anchoPuenteUtil = getWidth() - (margenPuente * 2);
        int paso = anchoPuenteUtil / 6;

        for (int i = 0; i < colaPasarela.size(); i++) {
            Persona e = colaPasarela.get(i);

            if (e.getTipo() != 'L') {
                int xPos;
                int yPos;

                if (e.getTipo() == 'D') {
                    // Carril inferior (hacia derecha)
                    yPos = Y_PUENTE_BASE + 15;
                    xPos = margenPuente + (i * paso) + 20;
                    g.drawImage(imgDerecha, xPos, yPos, anchoPersona, altoPersona, this);
                } else {
                    // Carril superior (hacia izquierda)
                    yPos = Y_PUENTE_BASE - 25;
                    xPos = margenPuente + (i * paso) + 20;
                    g.drawImage(imgIzquierda, xPos, yPos, anchoPersona, altoPersona, this);
                }

                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(e.getId()), xPos + 8, yPos - 5);
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("ESTADO PASARELA", getWidth() / 2 - 50, 50);
    }

    public synchronized void pintaColaIzquierda(int id) {
        colaIzquierda.add(new Persona(id, 'I'));
        repaint();
    }

    public synchronized void pintaEntraIzquierda(int id) {
        quitaDeCola(colaIzquierda, id, 'I');
        entraEnPasarela(id, 'I');
        repaint();
    }

    public synchronized void pintaColaDerecha(int id) {
        colaDerecha.add(new Persona(id, 'D'));
        repaint();
    }

    public synchronized void pintaEntraDerecha(int id) {
        quitaDeCola(colaDerecha, id, 'D');
        entraEnPasarela(id, 'D');
        repaint();
    }

    public synchronized void pintaSale(int id) {
        for (int i = 0; i < colaPasarela.size(); i++) {
            if (colaPasarela.get(i).getId() == id) {
                colaPasarela.set(i, new Persona(-1, 'L')); // Liberar posición
                break;
            }
        }
        repaint();
    }

    private void quitaDeCola(ArrayList<Persona> cola, int id, char tipo) {
        Persona e = new Persona(id, tipo);
        if (cola.contains(e)) {
            // Buscamos y borramos la primera ocurrencia de ese ID
            for (int i = 0; i < cola.size(); i++) {
                if (cola.get(i).getId() == id) {
                    cola.remove(i);
                    break;
                }
            }
        }
    }

    private void entraEnPasarela(int id, char tipo) {
        Persona e = new Persona(id, tipo);
        // Ocupar el primer hueco libre
        for (int i = 0; i < colaPasarela.size(); i++) {
            if (colaPasarela.get(i).getTipo() == 'L') {
                colaPasarela.set(i, e);
                return;
            }
        }
    }
}
