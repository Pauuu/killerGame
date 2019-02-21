package killergame;

import java.awt.Color;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

class KillerGame extends JFrame {

    // constantes
    private final int FRAME_WIDTH = 920;
    private final int FRAME_HEIGHT = 800;


    // objetos visibles del juego
    private ArrayList<VisibleObject> visibleObjects;

    // conexiones
    private KillerServer killerServer;
    private VisualHandler killerRight;
    private VisualHandler killerLeft;
    private ArrayList<KillerPad> killerPads;

    // graficos
    private Viewer viewer;

    public KillerGame() {

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        // instanciar elementos
        this.visibleObjects = new ArrayList<>();
        this.killerPads = new ArrayList<>();
        this.killerServer = new KillerServer(this);

        //añadir comunicaciones
        this.startServer();
        this.startVisualModels();

        //ojo poner un contador para "encender" los clientes despues
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
        Ball a = new Ball(this, 1, 0, 300, 300);
//        Ball b = new Ball(this, 65, 0, 30, 30);

        a.setColor(Color.PINK);

        this.createViewer(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        this.pack();
        this.setVisible(true);
        this.startGame();

        try {
            Thread.sleep(900);
        } catch (InterruptedException ex) {
            Logger.getLogger(KillerGame.class.getName()).log(Level.SEVERE, null, ex);
        }

        // a.disparo();
    }

    private void createViewer(int width, int height) {
        this.viewer = new Viewer(this, width, height);
        this.getContentPane().add(this.viewer);
    }

    private void startGame() {

        //inicial los todos los elemetnos
        for (int pos = 0; pos < this.visibleObjects.size(); pos++) {
            if (this.visibleObjects.get(pos) instanceof Autonomous) {
                new Thread((Autonomous) this.visibleObjects.get(pos)).start();
            }
        }

        //input del usuario?
        //--crear un hilo por cada nuevo elemento--
        //pintar todo
        new Thread(this.viewer).start();

    }

    private void startServer() {
        new Thread(this.killerServer).start();
    }

    private void startVisualModels() {
        // como es que no salta excepcion???????
        new Thread(this.killerRight).start();
        new Thread(this.killerLeft).start();
    }

    private void aplicarRegla(int regla, Alive vObj) {
        // --discriminar reglas--

        switch (regla) {
            case 1:
                vObj.invertirVelocidades();
                break;

            case 2:
                vObj.kill();
                break;

            default:
                break;
        }

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
        VisibleObject vObj;

        for (int pos = 0; pos < this.visibleObjects.size(); pos++) {
            vObj = this.visibleObjects.get(pos);

            if (objTest != vObj) {

                //comprobar si colision
                if (objTest.getHitBox().intersects(vObj.getHitBox())) { //lamar a las reglas para conocer que han de hacer

                    int regla = KillerRules.testColision(objTest, vObj);
                    this.aplicarRegla(regla, objTest);
                }
            }
        }
    }

    // conexinoes
    public synchronized Boolean tryConnectLeftKiller(Socket cliSock, String cliAddr) {

        if (this.killerLeft != null) {
            return false;   //============= NextKiller ya existe ===========>>>>
        }

        //crear nuevo previousKiller
        this.killerLeft = new VisualHandler(this, cliSock, cliAddr);

        //iniciar nuevo hilo 
        new Thread(this.killerLeft).start();

        System.out.println("KG: conexion izquirda exitosa");
        return true;        //============ NextKiller creado y añadido=======>>>
    }

    public synchronized Boolean tryConnectRightKiller(Socket cliSock, String cliAddr) {

        if (this.killerRight != null) {
            return false;   //========= PreviousKiller ya existe ===========>>>>
        }

        // crear nuevo nextKiller
        this.killerRight = new VisualHandler(this, cliSock, cliAddr);

        // iniciar nuevo hilo 
        new Thread(this.killerRight).start();

        System.out.println("KG: conexion derecha exitosa");
        return true;        //======== PreviousKiller creado y añadido=======>>>
    }

    public synchronized void connectKillerPad(Socket cliSock, String cliAddr) {

        // crear nuevo killerPad
        KillerPad kp = new KillerPad(this, cliSock, cliAddr);

        // añadir killerPad a la lista
        this.killerPads.add(kp);

        // iniciar nuevo hilo
        new Thread(kp).start();
        
        System.out.println("KG: conexoin killerPad exitosa");
    }

    public void addVisibleObject(VisibleObject vObj) {
        this.visibleObjects.add(vObj);
    }

    public void testColision(Alive objTest) {

        //mira si ha tocado con los marcos de la pantalla
        this.testTocadoMargenPantalla(objTest);

        //mira si ha colisionado con algun objeto visible
        this.testColisionVisibleObjects(objTest);

    }

    // Getters & setters
    public ArrayList getVisibleObjects() {
        return this.visibleObjects;
    }

    public int getFrameHeight() {
        return this.FRAME_HEIGHT;
    }

    public int getFrameWidth() {
        return this.FRAME_WIDTH;
    }

}
