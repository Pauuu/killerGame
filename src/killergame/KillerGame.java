package killergame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

class KillerGame extends JFrame {

    private final int FRAME_WIDTH = 1800;
    private final int FRAME_HEIGHT = 400;

    private ArrayList<KillerClient> killerClients;

    /*  hace falta tener todas las listas de cada uno; basta diferenciar 
    *   entre los static y alive; o solo una lista de visibleObjects y hacer un 
    *   "instance of" para crear los threads de los alive?
     */ //creo q instanceOf es la mejor solución
    private ArrayList<Autonomous> autonomousObjects; //cambiar a alive?

//    private KillerServerHandler killerServerHandler;
    private KillerServer killerServer;

    private Viewer viewer;

    public KillerGame() {

        //setear parametros iniciales
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        this.setVisible(true);

        //añadir elementos
        this.killerClients = new ArrayList<>(); //x si es cliente de mas maquinas
        this.autonomousObjects = new ArrayList<>();
        this.killerServer = new KillerServer(this);

        //añadir comunicaciones
        this.startServer(this.killerServer);
        this.addClient();
//        this.startClient();

        //crear y añadir elementos graficos
        this.createViewer(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        this.createVisibleObjects();

        //empezar el juego (colisiones, etc)
        this.startGame();

    }

    public void addAutonomousObj(Autonomous aObj) {
        this.autonomousObjects.add(aObj);
    }

    private void addClient() {
        this.killerClients.add(new KillerClient(this));
    }

    private void createViewer(int width, int height) {
        this.viewer = new Viewer(this, width, height);
        this.getContentPane().add(this.viewer);
    }

    private void createVisibleObjects() {
        Autonomous bola = new Autonomous(this, 10, 49);
        bola.setPosX(30);
        bola.setPosY(30);
        this.addAutonomousObj(bola);
    }

    private void startClient() {
        //de momento solo uno
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

        while (true) {
            for (Autonomous aObj : autonomousObjects) {
                kr.comprobarColision(aObj);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(KillerGame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

//        this.killerClients.get(0).sendBola(this.autonomousObjects.get(0));
    }

    private void startServer(KillerServer ks) {
        new Thread(ks).start();
    }

    //Getters & setters
    public ArrayList getAoutonomousObjects() {
        return this.autonomousObjects;
    }
}
