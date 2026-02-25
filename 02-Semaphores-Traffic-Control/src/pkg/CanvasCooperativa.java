package pkg;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.ArrayList;


public class CanvasCooperativa extends Canvas {

    // Clase interna que representa un coche o plaza libre
    class Elemento {

        int id;
        char tipo; // 'C' = camión, 'T' = tractor, 'L' = libre

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
    
    private final Image imgTractor, imgCamion, imgFondo;

    // Colas para representar el estado visual
    private final ArrayList<Elemento> colaCamiones = new ArrayList<>();
    private final ArrayList<Elemento> colaTractores = new ArrayList<>();
    private final ArrayList<Elemento> colaCooperativa = new ArrayList<>();

    // Constantes de dibujo
    private final int anchoTractorParking = 500;
    private final int anchoCamionParking = 250;
    private final int altoCochesParking = 200;
    private final int anchoTractorCola = 150;
    private final int anchoCamionCola = 100;
    private final int altoTractorCola = 70;
    private final int altoCamionCola = 70;
    private final int espacioTractor = 150;
    private final int espacioCamion = 100;
    private final int numPuestos = 3;

    // Posiciones en pantalla
    private final int[] posColaCamion = {50, 60, 60};
    private final int[] posColaTractor = {50, 160, 60};

    // Constructor
    public CanvasCooperativa(int ancho, int alto) throws InterruptedException {
        super.setSize(ancho, alto);
        super.setBackground(Color.white);

        // Carga de imágenes
        imgFondo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/fondo.png"));
        imgTractor = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/tractor.png"));
        imgCamion = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pkg/imagenes/camion.png"));

        // Espera a que las imágenes se carguen
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(imgFondo, 0);
        tracker.addImage(imgTractor, 1);
        tracker.addImage(imgCamion, 2);
        tracker.waitForAll();

        // Inicializa la cooperativa con plazas libres ('L')
        for (int i = 0; i < 3; i++) {
            colaCooperativa.add(new Elemento(-1, 'L')); // 'L' = Libre
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

        g.drawImage(imgFondo, 0, 0, 1000, 500, this);
        Font f1 = new Font("Rockwell", Font.BOLD, 20);
        g.setFont(f1);

        // Cola camiones
        g.setColor(Color.black);
        g.drawString("Cola Camiones", posColaCamion[0], posColaCamion[1] - 10);
        g.setColor(Color.gray);
        g.fillRect(posColaCamion[0], posColaCamion[1], 950 - posColaCamion[0] - 10, posColaCamion[2]);
        for (int i = 0; i < colaCamiones.size(); i++) {
            Elemento e = colaCamiones.get(i);
            g.drawImage(imgCamion, posColaCamion[0] + espacioCamion * i, posColaCamion[1] - 15, anchoCamionCola, altoCamionCola, this);
            g.setColor(Color.black);
            g.drawString(String.valueOf(e.getId()), posColaCamion[0] + 50 + espacioCamion * i, posColaCamion[1] + 25);
        }

        // Cola tractores
        g.setColor(Color.black);
        g.drawString("Cola Tractores", posColaTractor[0], posColaTractor[1] - 10);
        g.setColor(Color.gray);
        g.fillRect(posColaTractor[0], posColaTractor[1], 950 - posColaTractor[0] - 10, posColaTractor[2]);
        for (int i = 0; i < colaTractores.size(); i++) {
            Elemento e = colaTractores.get(i);
            g.drawImage(imgTractor, posColaTractor[0] + espacioTractor * i, posColaTractor[1] - 15, anchoTractorCola, altoTractorCola, this);
            g.setColor(Color.black);
            g.drawString(String.valueOf(e.getId()), posColaTractor[0] + 70 + espacioTractor * i + 30, posColaTractor[1] + 40);
        }

        // Cooperativa
        g.setColor(Color.black);
        g.drawString("Cooperativa (3 plazas)", 415, 265);

        for (int i = 0; i < numPuestos; i++) {
            int x = 1000 - (43 + (i+1) * 275);  // ajustamos los coches en el parking
            int y = 275;
            Elemento e = colaCooperativa.get(i);

            // Fondo plaza
            g.setColor(Color.white);
            g.fillRect(1000 - (i * 275 + 300), 275, 200, 200);

            // Dibujo del coche si hay
            if (e.getTipo() != 'L') {
                Image img;
                if(e.getTipo() == 'T'){
                    img = imgTractor;
                    g.drawImage(img, x, y, anchoTractorParking, altoCochesParking, this);
                }
                    
                else{
                    img = imgCamion;
                    g.drawImage(img, x, y, anchoCamionParking, altoCochesParking, this);
                }
                    
                g.setColor(Color.black);
                g.drawString(Integer.toString(e.getId()), x+25, y+25);
            }
        }
    }
    
    public synchronized void pintaColaVehiculo(int id, char tipo) {
        if(tipo == 'C')
            colaCamiones.add(new Elemento(id, 'C'));
        else
            colaTractores.add(new Elemento(id, 'T'));

        repaint();
    }

    public synchronized void pintaEntraVehiculo(int id, char tipo, int pos) {
        Elemento libre = new Elemento(-1, 'L');
        Elemento e;
        
        if(tipo == 'C'){
            e = new Elemento(id, 'C');
            if(colaCamiones.contains(e))
                colaCamiones.removeFirst();
        }
        else{
            e = new Elemento(id, 'T');
            if(colaTractores.contains(e))
                colaTractores.removeFirst();
        }
        
        if(pos == 1 || pos == 2){
            colaCooperativa.set(pos-1, libre);
        }

        colaCooperativa.set(pos, e);

        repaint();
    }

    public synchronized void pintaSaleVehiculo(int id) {
        Elemento libre = new Elemento(-1, 'L');
        
        colaCooperativa.set(2, libre);

        repaint();
    }

}
