/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergamePau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
public class VisualHandler implements Runnable {

    private BufferedReader in;
    private String position;
    private KillerGame killerGame;
    private KillerClient killerClient;
    private PrintWriter out;
    private Socket socket = null;
    private String clientAddr;
    private String ip;
    private int port;

    private long controlTime;
    private Wall w;

    public VisualHandler(KillerGame kg, String position) {
        this.killerGame = kg;
        this.position = position;

        // crear killerClient
        this.killerClient = new KillerClient(this, this.position);

        // aseguro iniciar el cliente siempre
        this.startClient(); // ponerlo siempre en la excepcion de conexion no encotrada???

        //  this.tryConnection();
    }

    public void startClient() {
        new Thread(this.killerClient).start();
    }

    public void toctoc() {

        this.out.println("m&toc-toc");

    }

    public void killAll() {
        try {
            this.socket.close();
            this.socket = null;
            this.in = null;
            this.out = null;

        } catch (IOException ex) {
            Logger.getLogger(VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(String msj) {

        if (this.socket != null) {
            this.out.println(msj);
        }
    }

    private void processMessage(BufferedReader in, PrintWriter out) {
        String line;
        Boolean done = false;

        try {
            this.toctoc();
            while (!done) {

//                System.out.println("VH: Waiting for reading lines...");
                line = in.readLine();
                if (line.equalsIgnoreCase("m&toc-toc")) {
                    this.toctoc();
                    this.controlTime = System.currentTimeMillis();

                } else {

                    System.out.println("VH: mensaje recibido: " + line);

                    // si line null es que el cliente ha cerrado/perdido la conexion
                    if (line == null) {
                        System.err.println("VH: line == null. La linea recibida es "
                                + "un valor nulo \n");
                        done = true;
                        this.socket = null;

                    } else {
                        String[] message = line.trim().split("&");

//                    System.out.println("mensaje recibido >>>>> " + line);
//                    System.out.println("Message[1]: " + message[1]);
                        switch (message[1]) {

                            case "toc-toc":
//                            this.controlTime = System.currentTimeMillis();
                                this.out.println(line);

                            case "bye":
                                done = true;
                                break;

                            case "ball":
                                //o recibe el obj bola o cada parametro x separado
                                //cambiar coordenadas hardcodeadas
//                            this.killerGame.createAlive(new Ball(this.killerGame, 5, 5, 50, 50));

//                            System.out.println("VH: recivdo ball");
                                int x;
                                if (message[0].equalsIgnoreCase("r")) {
                                    x = 1;

                                } else {
//                                x = this.killerGame.getWidth()- 1;
                                    x = this.killerGame.getFrameWidth() - (int) Double.parseDouble(message[4]) - 1;

                                }
                                new Ball(
                                        this.killerGame,
                                        x, //posX
                                        (Double.parseDouble(message[3]) * this.killerGame.getFrameHeight()),//posY
                                        (int) Double.parseDouble(message[4]),//with
                                        (int) Double.parseDouble(message[5]),//height
                                        Double.parseDouble(message[6]),//velX
                                        Double.parseDouble(message[7]) //velY
                                );

                                break;

                            case "ks":
                                if (message[0].equalsIgnoreCase("r")) {
                                    x = 1;

                                } else {
//                                x = this.killerGame.getWidth()- 1;
                                    x = this.killerGame.getFrameWidth() - (int) Double.parseDouble(message[4]) - 1;

                                }
                                new KillerShip(
                                        this.killerGame,
                                        x, //posX
                                        (Double.parseDouble(message[3]) * this.killerGame.getFrameHeight()),//posY

                                        Double.parseDouble(message[6]), //velX
                                        Double.parseDouble(message[7]), //velY
                                        message[8], // ip
                                        Integer.parseInt(message[9]), // puerto
                                        message[10] // nombre de la nave
                                );

                                //r&ks&442.0&2.0&60&60&4.0&0.0&192.168.0.162&8000&fromPnew:jsjsjsj&ffffff
                                break;

                            case "cks":

//                            KillerPad.manageMessage(line, this.killerGame);
//                            break;
                                String ipOrigen = message[4];
                                int puertoOrigen = Integer.parseInt(message[5]);

                                // si:
                                // ip distinta (AND puerto igual OR distinto)
                                // OR
                                // port distinto
                                if ((!ipOrigen.equalsIgnoreCase(this.killerGame.getKillerServer().getIp())
                                        || (puertoOrigen != this.killerGame.getKillerServer().getServerPort()))) {

                                    KillerShip ks = KillerPad.ckeckKillerShip(
                                            this.killerGame,
                                            message[2], // ip nave
                                            puertoOrigen // puerto
                                    );

                                    // comprobar si existe la nave
                                    if (ks != null) {

                                        ks.doAction(message[3]);
                                        System.out.println("nave NO null");

                                    } else {
                                        System.out.println("VH: nave null \n");
                                        this.killerGame.getKillerRight().sendMessage(
                                                line
                                        );

                                        System.out.println(line + "\n");

                                    }
                                }

                                break;

                            case "ckk":

                                System.err.println("entrado en el ckk: ");
//                            
                                String ip = message[4];
                                int puerto = Integer.parseInt(message[5]);

                                // si:
                                // ip distinta (AND puerto igual OR distinto)
                                // OR
                                // port distinto
                                if ((!ip.equalsIgnoreCase(this.killerGame.getKillerServer().getIp())
                                        || (puerto != this.killerGame.getKillerServer().getServerPort()))) {

                                    String[] ipPort = message[2].split("/");
                                    String kpIp = ipPort[0];
                                    int kpPort = Integer.parseInt(ipPort[1]);

                                    System.err.println("entrado en el ckk: send ded ");

                                    //  KillerPad.manageMessage(line, this.killerGame);
                                    this.killerGame.sendDedNotification(
                                            message[4],
                                            Integer.parseInt(message[5]),
                                            kpIp,
                                            kpPort
                                    );
                                }
                                break;

                            default:
                                System.out.println("VH: msg (default): \n" + line);
                                break;
                        }
                    }
                }
            }
        } catch (Exception ex) {

            Logger.getLogger("VH: " + VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("VH: " + port + ex);

//                this.socket.close();
            this.socket = null;
            done = true;

        }
    }

    @Override
    public void run() {
        while (true) {

            try {
                //si no hay socket, seguir con el bucle
                if (this.socket != null) {

                    // setear PrintWriter (poder enviar msjs)
                    this.out = new PrintWriter(this.socket.getOutputStream(), true);

                    // setear BufferedReader (poder recibir msjs)
                    this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

//                    this.toctoc();
                    this.processMessage(this.in, this.out);

                }

//                this.socket = null;
                Thread.sleep(200);
            } catch (Exception ex) {
                Logger.getLogger(VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getPosition() {
        return this.position;
    }

    public KillerGame getKillerGame() {
        return this.killerGame;
    }

    public synchronized Socket getSocket() {
        return this.socket;
    }

    public synchronized void setConnection(Socket cliSock, int clientPort) {
        if (this.getSocket() == null) {

            // setear socket
            this.socket = cliSock;

            // setear la ip
            this.ip = this.socket.getInetAddress().getHostAddress();

            System.out.println("VH: IP:" + this.socket.getInetAddress().getHostAddress());

            // setear port del servidor
            this.port = clientPort;

            System.out.println("VH: toda conexion ok?");

//            this.toctoc();
            // marcamos el momento en el que se
            this.controlTime = System.currentTimeMillis();

        } else {
            System.err.println("VH: no se ha seteado el socket. Socket ya existe");
        }
    }

    public String getClientIp() {
        return this.ip;
    }

    public void setClientIP(String ip) {
        this.ip = ip;
    }

    public void setClientPort(int serverPort) {
        this.port = serverPort;
    }

    public int getPort() {
        return this.port;
    }

    void setSocket() {
        System.err.println("no  hay nada codeado aun");
    }

    public long getControlTime() {
        return this.controlTime;
    }

}