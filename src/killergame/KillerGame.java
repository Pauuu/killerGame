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

//    private KillerServerHandler killerServerHandler;
    private KillerServer killerServer;
    private int width;
    private int height;
    private Viewer viewer;

    public KillerGame() {
        this.setFrameSize(500, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(this.width, this.height);
        this.setVisible(true);

        this.killerClients = new ArrayList<>(); //x si es cliente de mas maquinas
        this.autonomousObjects = new ArrayList<>();
        this.killerServer = new KillerServer(this);

        this.startServer(this.killerServer);
        this.addClient();
        this.startClient();
        this.addViewer(this.width, this.height);
        this.addVisibleObjects();

        this.startGame();

    }

    public void addAutonomousObj(Autonomous aObj) {
        this.autonomousObjects.add(aObj);
    }

    private void addClient() {
        this.killerClients.add(new KillerClient(this));
    }

    private void addViewer(int width, int height) {
        this.viewer = new Viewer(this, width, height);
        this.getContentPane().add(this.viewer);
    }

    private void addVisibleObjects() {
        Autonomous bola = new Autonomous(this);
        bola.setPosX(30);
        bola.setPosY(30);
        this.addAutonomousObj(bola);
    }

    private void startClient() {
        //deberia haber solo uno de momento
        this.killerClients.get(0).makeContact();
    }

    private void startGame() {
        KillerRules kr = new KillerRules();

        //inicial los todos los elemetnos
        for (Autonomous aObj : autonomousObjects) {
            new Thread(aObj).start();
        }

        //input del usuario
        //comprobar colisiones/normas/etc
        //--crear un hilo por cada nuevo elemento--
        //pintar todo
        new Thread(this.viewer).start();

        try {
            Thread.sleep(900);
        } catch (InterruptedException ex) {
            Logger.getLogger(KillerGame.class.getName()).log(Level.SEVERE, null, ex);
        }

//        this.killerClients.get(0).sendBola(this.autonomousObjects.get(0));

        while (true) {
            for (Autonomous aObj : this.autonomousObjects) {
                kr.comprobarColision(aObj);
            }
        }
    }

    private void startServer(KillerServer ks) {
        new Thread(ks).start();
    }

    //Getters & setters
    public ArrayList getAoutonomousObjects() {
        return this.autonomousObjects;
    }

    private void setFrameSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
