package killergame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

class KillerGame extends JFrame {

    private ArrayList<KillerClient> killerClients;

    /*hace falta tener todas las listas de cada uno, solo basta diferenciar 
    *   entre los static y alive o solo una lista de visibleObjects y hacer un 
    *   "instance of" para crear los threads de los alive?
     */ //creo q instanceOf es la mejor solucion
    private ArrayList<Autonomous> autonomousObjects; //cambiar a alive?

    private KillerServer killerServer;
    private int width;
    private int height;
    private Viewer viewer;

    public KillerGame() {
        this.setFrameSize(300, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(this.width, this.height);
        this.setVisible(true);

        this.killerClients = new ArrayList<>(); //x si es cliente de mas maquinas
        this.autonomousObjects = new ArrayList<>();
        this.killerServer = new KillerServer(this);

        this.addViewer();
        this.addVisibleObjects();

        this.startGame();

    }

    private void startGame() {
        while (true) {

            //input del usuario
            //comprobar colisiones/normas/etc
            //--crear un hilo por cada nuevo elemeento--
            for (Autonomous ao : autonomousObjects) {
                new Thread(ao).start();
            }

            //pintar todo
            new Thread(this.viewer).start();

            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(KillerGame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void addVisibleObjects() {
        this.addBola(this.viewer, 60, 60);
    }

    private void addViewer() {

        this.viewer = new Viewer(this, this.height, this.width);
        this.getContentPane().add(this.viewer);
    }

    private void addBola(Viewer v, int posX, int posY) {
        Autonomous bola = new Autonomous(this);
        bola.setPosX(posX);
        bola.setPosY(posY);
        this.autonomousObjects.add(bola);

    }

    private void setFrameSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //Getters & setters
    public ArrayList getAoutonomousObjects() {
        return this.autonomousObjects;
    }
}
