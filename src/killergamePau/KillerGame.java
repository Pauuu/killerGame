package killergamePau;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

class KillerGame extends JFrame {

    // constantes
    private final int FRAME_WIDTH = 700;
    private final int FRAME_HEIGHT = 700;

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
        this.createStartVisualHandlers();
        this.createStartServer();
        this.createJFrameForKillerClients();

        //crear y añadir elementos graficos
        Ball a = new Ball(this, 300, 300, 60, 60, 4, 3);
        this.createViewer(this.FRAME_WIDTH, this.FRAME_HEIGHT);
        this.pack();
        this.setVisible(true);
        this.startGame();

    }

    public KillerPad searchKillerPad(String ip, int port) {
        for (int pos = 0; pos < this.killerPads.size(); pos++) {

            //  si ip y puerto coinciden
            if (this.killerPads.get(pos).getIp().equalsIgnoreCase(ip)
                    && this.killerPads.get(pos).getPort() == port) {
                System.out.println("KCenbctrado");
                return this.killerPads.get(pos);
            }

        }
        System.out.println("KCno encontrado");
        return null;
    }

    public KillerShip checkKillerShip(String ip, int port) {
        for (int pos = 0; pos < this.getVisibleObjects().size(); pos++) {

            // cerar en game metodo para obtener array de objs en especiico????
            // discriminar los killerShips
            if (this.getVisibleObjects().get(pos) instanceof KillerShip) {

                // buscar el que tenga misma id
                if (((KillerShip) this.getVisibleObjects().get(pos)).getIp().equalsIgnoreCase(ip)) {
                    return (KillerShip) this.getVisibleObjects().get(pos);
                }
            }
        }

        return null;
    }

    public VisualHandler getVisualHandler(String posicion) {

        if (posicion.equalsIgnoreCase("r")) {
            return this.killerRight;

        } else if (posicion.equalsIgnoreCase("l")) {
            return this.killerLeft;

        } else {
            System.out.println("KG: no se ha devuelto nada");
            return null;
        }
    }

    public void addKillerPad(KillerPad kp) {
        this.killerPads.add(kp);
    }

    public void addVisibleObject(VisibleObject vObj) {
        this.visibleObjects.add(vObj);
    }

    public void moveShip(String shipId, int velX, int velY) {
        for (int pos = 0; pos < this.visibleObjects.size(); pos++) {

            if (this.visibleObjects.get(pos) instanceof KillerShip) {
                KillerShip ks = (KillerShip) this.visibleObjects.get(pos);

                if (ks.getIp() == shipId) {
                    ks.setVelX(velX);
                    ks.setVelY(velY);
                    return;
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

    public void sendDedNotification(String serverIp, int serverPort, String ip, int port) {

        // busca killer pad
        KillerPad kPad = this.searchKillerPad(ip, port);

        // si esta: enviar msj de muerte
        if (kPad != null) {
            kPad.sendMessageToMobile("ded");
            return; //===================================================>>>
        }

        // si no está: enviar msj por el VH de la derecha
        this.killerRight.sendMessage(
                "m"
                + "&" + "ckk"
                + "&" + ip + "/" + port
                + "&" + "ded"
                + "&" + serverIp
                + "&" + serverPort
        );

        System.err.println("mensaje enviado: "
                + "m"
                + "&" + "ckk"
                + "&" + ip + "/" + port
                + "&" + "ded"
                + "&" + serverIp
                + "&" + serverPort
        );

    }

    private String createMessageBall(Ball b) {
        String msg;

        msg = "ball"
                + "&" + b.getPosX()
                + "&" + b.getPosY()
                + "&" + b.getWidth()
                + "&" + b.getHeight()
                + "&" + b.getVelX()
                + "&" + b.getVelY();
        return msg;
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

            case 3:
                vObj.kill();
                ((KillerShip) vObj).sendDedNotification();
                break;

            default:
                break;
        }

    }

    private void createJFrameForKillerClients() {
        JFrame ventanaConfiguracion = new JFrame("Configuracion killer client");
        ventanaConfiguracion.setLayout(new GridBagLayout());
        ventanaConfiguracion.setSize(500, 200);

        JLabel jlKillerRight = new JLabel("Right killer");
        JLabel jlKillerLeft = new JLabel("Left killer");

        JTextField jtfKillerRightIp = new JTextField("localhost", 16);
        JTextField jtfKillerLeftIp = new JTextField("localhost", 16);

        JTextField jtfKillerRightPort = new JTextField("8001", 5);
        JTextField jtfKillerLeftPort = new JTextField("8001", 5);

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
                if (killerLeft.getClientIp() == null) {
                    killerLeft.setClientIP(jtfKillerLeftIp.getText());
                }

                // killerLeft set port 
                if (killerLeft.getPort() == 0) {
                    killerLeft.setClientPort(Integer.parseInt(jtfKillerLeftPort.getText()));
                }

                // killer right set ip
                if (killerRight.getClientIp() == null) {
                    killerRight.setClientIP(jtfKillerRightIp.getText());
                }

                // killer right set port
                if (killerRight.getPort() == 0) {
                    killerRight.setClientPort(Integer.parseInt(jtfKillerRightPort.getText()));
                }

                System.out.println("KG: cerrando ventana de configuracion");
                ventanaConfiguracion.dispose();
            }
        });

        // añadir listener al boton acepatar
    }

    private void createStartServer() {
        this.killerServer = new KillerServer(this);
        new Thread(this.killerServer).start();
    }

    private void createViewer(int width, int height) {
        this.viewer = new Viewer(this, width, height);
        this.getContentPane().add(this.viewer);
    }

    private void createStartVisualHandlers() {

        this.killerLeft = new VisualHandler(this, "l");
        this.killerRight = new VisualHandler(this, "r");

        new Thread(this.killerLeft).start();
        new Thread(this.killerRight).start();

    }

    private void startGame() {

//        //inicial los todos los elemetnos
//        for (int pos = 0; pos < this.visibleObjects.size(); pos++) {
//            if (this.visibleObjects.get(pos) instanceof Alive) {
//                new Thread((Alive) this.visibleObjects.get(pos)).start();
//            }
//        }
        //--crear un hilo por cada nuevo elemento--?
        //pintar todo
        new Thread(this.viewer).start();

    }

    /**
     * corregir y mejorar metodo
     *
     * @param objTest
     */
    private void testTocadoMargenPantalla(Alive objTest) {

        // marco derecho -----------------------------------
        if (objTest.posX >= objTest.killerGame.getFrameWidth() - objTest.width) {

            // nave
            if (objTest instanceof KillerShip) {

                if (this.killerRight.getSocket() != null) {

                    //eliminar objeto
                    objTest.kill();

                    // enviar objeto
                    this.killerRight.sendMessage(
                            this.killerRight.getPosition()
                            + "&" + "ks"
                            + "&" + objTest.getPosX()
                            + "&" + (objTest.getPosY() / (double) this.FRAME_HEIGHT)
                            + "&" + objTest.getWidth()
                            + "&" + objTest.getHeight()
                            + "&" + objTest.getVelX()
                            + "&" + objTest.getVelY()
                            + "&" + ((KillerShip) objTest).getIp()
                            + "&" + ((KillerShip) objTest).getPort()
                            + "&" + ((KillerShip) objTest).getName()
                    );

                } else {
                    // rebotar
                    objTest.setVelX(0);
                }

//                return;
            } else if (objTest instanceof Bullet) {
                objTest.kill();

                // bola
            } else {

                if (this.killerRight.getSocket() != null) {
                    //eliminar objeto
                    objTest.kill();

                    // enviar objeto
                    this.killerRight.sendMessage(
                            this.killerRight.getPosition()
                            + "&" + "ball"
                            + "&" + objTest.getPosX()
                            + "&" + (objTest.getPosY() / (double) this.FRAME_HEIGHT)
                            + "&" + objTest.getWidth()
                            + "&" + objTest.getHeight()
                            + "&" + objTest.getVelX()
                            + "&" + objTest.getVelY()
                    );

                } else {
                    // rebotar
                    objTest.setVelX(-Math.abs(objTest.velX));
                }

//                return;
            }
        }

        // marco izquierdo ---------------------------------------------------
        if (objTest.posX <= 0) {

            // nave
            if (objTest instanceof KillerShip) {

                if (this.killerLeft.getSocket() != null) {
                    //eliminar objeto
                    objTest.kill();

                    // enviar objeto
                    this.killerLeft.sendMessage(
                            this.killerLeft.getPosition()
                            + "&" + "ks"
                            + "&" + objTest.getPosX()
                            + "&" + (objTest.getPosY() / (double) this.FRAME_HEIGHT)
                            + "&" + objTest.getWidth()
                            + "&" + objTest.getHeight()
                            + "&" + objTest.getVelX()
                            + "&" + objTest.getVelY()
                            + "&" + ((KillerShip) objTest).getIp()
                            + "&" + ((KillerShip) objTest).getPort()
                            + "&" + ((KillerShip) objTest).getName()
                    );

                } else {
                    // parar
                    objTest.setVelX(0);
                }

//                return;
            } else if (objTest instanceof Bullet) {
                objTest.kill();
                // bola
            } else {

                if (this.killerLeft.getSocket() != null) {
                    //eliminar objeto
                    objTest.kill();

                    // enviar objeto
                    this.killerLeft.sendMessage(
                            this.killerLeft.getPosition()
                            + "&" + "ball"
                            + "&" + objTest.getPosX()
                            + "&" + (objTest.getPosY() / (double) this.FRAME_HEIGHT)
                            + "&" + objTest.getWidth()
                            + "&" + objTest.getHeight()
                            + "&" + objTest.getVelX()
                            + "&" + objTest.getVelY()
                    );

                } else {
                    // rebotar
                    objTest.setVelX(Math.abs(objTest.velX));
                }

//                return;
            }

        }

        // marco superior e inferior ---------------
        if ((objTest.posY >= objTest.killerGame.getFrameHeight() - objTest.height)
                || (objTest.posY <= 0)) {

            if (objTest instanceof KillerShip) {
                objTest.setVelY(0);

            } else if (objTest instanceof Bullet) {
                objTest.kill();
            } else {
                if (objTest.posY <= 0) {
                    objTest.setVelY(Math.abs(objTest.velY));

                } else {
                    objTest.setVelY(-Math.abs(objTest.velY));

                }

            }

        }

//        // marco izquierdo ------------------------------------
//        if (objTest.posX <= 0) {
//
//            if (this.killerLeft.getSocket() != null) {
//
//                // eliminar objeto
//                objTest.kill();
//
//                // enviar objeto
//                this.killerLeft.sendMessage(
//                        this.killerLeft.getPosition()
//                        + "&" + "ball"
//                        + "&" + objTest.getPosX()
//                        + "&" + objTest.getPosY()
//                        + "&" + objTest.getWidth()
//                        + "&" + objTest.getHeight()
//                        + "&" + objTest.getVelX()
//                        + "&" + objTest.getVelY()
//                );
//
//            } else {
//                // rebotar
//                objTest.invertirVelX();
//            }
//
//            return;
//
//        }
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

    // Getters & setters
    public ArrayList getVisibleObjects() {
        return this.visibleObjects;
    }

    public KillerServer getKillerServer() {
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

    public ArrayList<KillerPad> getKillerPads() {
        return this.killerPads;
    }

}
