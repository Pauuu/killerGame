package killergame;

import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

class KillerGame extends JFrame {

    private final int FRAME_WIDTH = 920;
    private final int FRAME_HEIGHT = 800;

    private ArrayList<KillerClient> killerClients;

    /*  hace falta tener todas las listas de cada uno; basta diferenciar 
    *   entre los static y alive; o solo una lista de visibleObjects y hacer un 
    *   "instance of" para crear los threads de los alive?
     */ //creo q instanceOf es la mejor solución
    private ArrayList<Autonomous> autonomousObjects; //cambiar a alive? a VisibleObject?
    private PreviousKiller previousKiller;
    private NextKiller nextKiller;

//    private KillerServerHandler killerServerHandler;
    private KillerServer killerServer;

    private Viewer viewer;

    public KillerGame() {

        //setear parametros iniciales
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.setSize(this.FRAME_WIDTH, this.FRAME_HEIGHT);

        //añadir elementos
        this.killerClients = new ArrayList<>(); //x si es cliente de mas maquinas
        this.autonomousObjects = new ArrayList<>();
        this.killerServer = new KillerServer(this);

        //añadir comunicaciones
        this.startServer(this.killerServer);

        try {
            Thread.sleep(90);
        } catch (InterruptedException ex) {
            Logger.getLogger(KillerGame.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.addClient("localhost");
        this.addClient("localhost");
        this.addClient("localhost");
        this.startClient(0);
        this.startClient(1);
        this.startClient(2);

        //crear y añadir elementos graficos
        this.createViewer(this.FRAME_WIDTH, this.FRAME_HEIGHT);
//        this.createVisibleObjects();

        this.createBall(20, 20, 100, 500);

        this.pack();
        this.setVisible(true);

        //empezar el juego (colisiones, etc)
        this.startGame();

    }

    public void addAutonomousObj(Autonomous aObj) {
        this.autonomousObjects.add(aObj);
    }

    private void addClient(String ip) {
        this.killerClients.add(new KillerClient(this, ip));
    }

    private void createBall(int witdh, int height, int posX, int posY) {
        Ball bola = new Ball(this, witdh, height);
        bola.setPosX(posX);
        bola.setPosY(posY);
        this.addAutonomousObj(bola); //necesario?
    }

    private void createViewer(int width, int height) {
        this.viewer = new Viewer(this, width, height);
        this.getContentPane().add(this.viewer);
    }

    private void createVisibleObjects() {
        Ball bola = new Ball(this, 20, 20);
        bola.setPosX(30);
        bola.setPosY(30);
        this.addAutonomousObj(bola);
    }

    private void startClient(int index) {
        //de momento solo uno
        this.killerClients.get(index).makeContact();
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

    }

    public synchronized Boolean tryConnectPreviousKiller(Socket cliSock, String cliAddr) {
        if (this.previousKiller != null) {
            return false;   //============= NextKiller ya existe ===========>>>>
        }

        //crear nuevo previousKiller
        PreviousKiller pk = new PreviousKiller(this, cliSock, cliAddr);

        //iniciar nuevo hilo 
        new Thread(pk).start();

        //almacenar variable en atributo de clase
        this.previousKiller = pk;
        return true;        //============ NextKiller creado y añadido=======>>>
    }

    public synchronized Boolean tryConnectNextKiller(Socket cliSock, String cliAddr) {
        if (this.nextKiller != null) {
            return false;   //========= PreviousKiller ya existe ===========>>>>
        }

        //crear nuevo nextKiller
        NextKiller nk = new NextKiller(this, cliSock, cliAddr);

        //iniciar nuevo hilo 
        new Thread(nk).start();

        //almacenar variable en atributo de clase
        this.nextKiller = nk;
        return true;        //======== PreviousKiller creado y añadido=======>>>
    }

    private void startServer(KillerServer ks) {
        new Thread(ks).start();
    }

    public void testColision() {

    }

    //Getters & setters
    public ArrayList getAoutonomousObjects() {
        return this.autonomousObjects;
    }

    public PreviousKiller getPreviousKiller() {
        return this.previousKiller;
    }

    public NextKiller getNextKiller() {
        return this.nextKiller;
    }

    public int getFrameHeight() {
        return this.FRAME_HEIGHT;
    }

    public int getFrameWidth() {
        return this.FRAME_WIDTH;
    }

    public void setNextKiller(NextKiller nk) {
        this.nextKiller = nk;
    }

    public void setPreviousKiller(PreviousKiller pk) {
        this.previousKiller = pk;
    }

}
