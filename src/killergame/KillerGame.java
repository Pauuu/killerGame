package killergame;

import java.awt.Color;
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
//    private ArrayList<Autonomous> autonomousObjects; //cambiar a alive? a VisibleObject?
    private ArrayList<VisibleObject> visibleObjects; //cambiar a alive? a VisibleObject?
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
//        this.autonomousObjects = new ArrayList<>();
        this.visibleObjects = new ArrayList<>();
        this.killerServer = new KillerServer(this);

        //añadir comunicaciones
        this.startServer(this.killerServer);

        //ojo poner un contador para poner los clientes despues
        try {
            Thread.sleep(90);
        } catch (InterruptedException ex) {
            Logger.getLogger(KillerGame.class.getName()).log(Level.SEVERE, null, ex);
        }

//        this.addClient("localhost");
//        this.addClient("localhost");
//        this.addClient("localhost");
//        this.startClient(0);
//        this.startClient(1);
//        this.startClient(2);
        //crear y añadir elementos graficos
//        this.createVisibleObjects();
//        this.createBall(20, 20, 100, 500);
        Ball a = new Ball(this, 5, 5, 30, 30);
        Ball b = new Ball(this, 59, 5, 30, 30);

//        a.setColor(Color.red);

        this.createAlive(a);
        

        this.createViewer(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        this.pack();
        this.setVisible(true);
        this.startGame();
    }

    private void addClient(String ip) {
        this.killerClients.add(new KillerClient(this, ip));
    }

    private void createViewer(int width, int height) {
        this.viewer = new Viewer(this, width, height);
        this.getContentPane().add(this.viewer);
    }

    private void startClient(int index) {
        //de momento solo uno
        this.killerClients.get(index).makeContact();
    }

    private void startGame() {

        //inicial los todos los elemetnos
        for (int pos = 0; pos < this.visibleObjects.size(); pos++) {
            if (this.visibleObjects.get(pos) instanceof Autonomous) {
                new Thread((Autonomous) this.visibleObjects.get(pos)).start();
            }
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

    public void createAlive(Alive aObj) {
        this.addVisibleObject(aObj);
    }

    private void startServer(KillerServer ks) {
        new Thread(ks).start();
    }

    private void addVisibleObject(VisibleObject vObj) {
        this.visibleObjects.add(vObj);
    }

    private void aplicarRegla(int regla, Alive vObj) {
        vObj.invertirVelocidades();
    }

    private void testTocadoMargenPantalla(Alive objTest) {
        //comprobar si ha chocado con el marco
        if ((objTest.posX >= objTest.killerGame.getFrameWidth() - objTest.witdh)
                || (objTest.posX <= 0)) {

            objTest.invertirVelX();
        }

        //mira si colision en eje Y (marco pantalla)
        if ((objTest.posY >= objTest.killerGame.getFrameHeight() - objTest.height)
                || (objTest.posY <= 0)) {

            objTest.invertirVelY();
        }
    }

    private void testColisionVisibleObjects(Alive objTest) {
        for (VisibleObject vObj : visibleObjects) {

            //si no es el mismo
            if (objTest != vObj) {

                //comprobar si colision
                if (objTest.getHitBox().intersects(vObj.getHitBox())) { //lamar a las reglas para conocer que han de hacer
                    int regla = KillerRules.testColision(objTest, vObj);
                    this.aplicarRegla(regla, objTest);

//                    System.out.println("killergame.KillerGame.testColision()");
                }

            }
        }
    }

    public void testColision(Alive objTest) {

        //mira si ha tocado con los marcos de la pantalla
        this.testTocadoMargenPantalla(objTest);

        //mira si ha colisionado con algun objeto visible
        this.testColisionVisibleObjects(objTest);

    }

    //Getters & setters
//    public ArrayList getAoutonomousObjects() {
//        return this.autonomousObjects;
//    }
    public ArrayList getVisibleObjects() {
        return this.visibleObjects;
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
