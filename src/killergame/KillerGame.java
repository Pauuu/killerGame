package killergame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

class KillerGame extends JFrame {

    // constantes
    private final int FRAME_WIDTH = 500;
    private final int FRAME_HEIGHT = 500;

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

        //añadir comunicaciones
        this.createVisualHandlers();
        this.createStartServer();
        this.createJFrameForKillerClients();

        //ojo poner un contador para "encender" los clientes despues
        try {
            Thread.sleep(90);
        } catch (InterruptedException ex) {
            Logger.getLogger(KillerGame.class.getName()).log(Level.SEVERE, null, ex);
        }

        //crear y añadir elementos graficos
        Ball a = new Ball(this, 0, 0, 30, 30);

        a.setColor(Color.ORANGE);

        this.createViewer(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        this.pack();
        this.setVisible(true);
        this.startGame();
        
        
        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            Logger.getLogger(KillerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.killerRight.sendMessage("ball");
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

        //--crear un hilo por cada nuevo elemento--?
        //pintar todo
        new Thread(this.viewer).start();

    }

    private void createVisualHandlers() {
        this.killerLeft = new VisualHandler(this, 'l');
        this.killerRight = new VisualHandler(this, 'r');
    }

    private void createStartServer() {
        this.killerServer = new KillerServer(this);
        new Thread(this.killerServer).start();
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
                if (objTest.getHitBox().intersects(vObj.getHitBox())) {
                    //lamar a las reglas para conocer que han de hacer

                    int regla = KillerRules.testColision(objTest, vObj);
                    this.aplicarRegla(regla, objTest);
                }
            }
        }
    }

    // conexinoes
    public VisualHandler getVisualHandler(char posicion) {
        if (posicion == 'r') {
            return this.killerRight;

        } else if (posicion == 'l') {
            return this.killerLeft;

        } else {
            System.out.println("KG: no se ha devuelto nada");
            return null;
        }
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

    private void createJFrameForKillerClients() {
        JFrame ventanaConfiguracion = new JFrame("Configuracion killer client");
        ventanaConfiguracion.setLayout(new GridBagLayout());
        ventanaConfiguracion.setSize(500, 200);

        JLabel jlKillerRight = new JLabel("Right killer");
        JLabel jlKillerLeft = new JLabel("Left killer");

        JTextField jtfKillerRightIp = new JTextField("localhost", 16);
        JTextField jtfKillerLeftIp = new JTextField("localhost", 16);

        JTextField jtfKillerRightPort = new JTextField("port", 5);
        JTextField jtfKillerLeftPort = new JTextField("port", 5);

        JButton jbAceptar = new JButton("Aceptar");
//        jbAceptar.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        ventanaConfiguracion.add(jlKillerRight, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        ventanaConfiguracion.add(jtfKillerRightIp, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        ventanaConfiguracion.add(jtfKillerRightPort, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        ventanaConfiguracion.add(jlKillerLeft, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        ventanaConfiguracion.add(jtfKillerLeftIp, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        ventanaConfiguracion.add(jtfKillerLeftPort, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        ventanaConfiguracion.add(jbAceptar, gbc);

        ventanaConfiguracion.setVisible(true);

        jbAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // killerLeft set ip
                killerLeft.setIP(jtfKillerLeftIp.getText());

                // killerLeft set port 
                killerLeft.setPort(Integer.parseInt(jtfKillerLeftPort.getText()));

                // killer right set ip
                killerRight.setIP(jtfKillerRightIp.getText());

                // killer right set port
                killerRight.setPort(Integer.parseInt(jtfKillerRightPort.getText()));

                // arrancar hilos de los clientes
                // -- de esta manera solo se iniciaran si se pulsa el btn aceptar 
                // (no se va a pulsar en todos los lados --
//                killerRight.startClient();
//                killerLeft.startClient();

                // cerrar ventana (?)
                System.out.println("KG: cerrando ventana de configuracion");
                ventanaConfiguracion.dispose();
            }
        });

        // añadir listener al boton acepatar
    }

    // Getters & setters
    public ArrayList getVisibleObjects() {
        return this.visibleObjects;
    }
    
    public KillerServer getKillerServer(){
        return this.killerServer;
    }

    public int getFrameHeight() {
        return this.FRAME_HEIGHT;
    }

    public int getFrameWidth() {
        return this.FRAME_WIDTH;
    }

    public VisualHandler getKillerLeft() {
        return this.killerLeft;
    }

    public VisualHandler getKillerRight() {
        return this.killerRight;
    }
    
    

}
