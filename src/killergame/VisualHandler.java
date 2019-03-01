/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JTextField;

/**
 *
 * @author pau
 */
public class VisualHandler implements Runnable {

    private BufferedReader in;
    private char position;
    private KillerGame killerGame;
    private KillerClient killerClient;
    private PrintWriter out;
    private Socket socket = null;
    private String clientAddr;
    private String ip;
    private int port;

    public VisualHandler(KillerGame kg, char position) {
        this.killerGame = kg;
        this.position = position;

        this.killerClient = new KillerClient(this, this.socket, this.position);

        //  this.tryConnection();
    }

    public synchronized void setKiller(Socket cliSock, String cliAddr) {

        // comprobar si conexion establecida
        // probar con Socket.isConnected() para cerrar los sockets
        if (this.getSocket() != null) {
            System.out.println("KG: soket left ocupado");
            return;  //========= KillerLeft ya existe ======================>>>>
        }
        
        this.setSocket(cliSock);
        this.setClientAddress(cliAddr);

        //iniciar y crearF nuevo hilo de visual handler
        this.startThread();

        System.out.println("KG: conexion izquierda exitosa");
    }

    /**
     * crea conexion con un server gracias al cliente creado
     */
    public void tryConnection() {

        new Thread(this.killerClient).start();
    }

    public void startThread() {
        new Thread(this).start();
    }

    public void setClientAddress(String cliAddr) {
        this.clientAddr = cliAddr;
    }

    public void sendBall() {
        System.out.println("VH sendBall out >> " + this.out);
        this.out.println("ball&tttt");
        System.out.println("ball sended");

    }

    /**
     * Comprobar si funciona
     *
     * @param msj
     */
    public void sendMessage(String msj) {
        this.out.println(msj);
    }

    public void setSocket(Socket cliSock) {
        this.socket = cliSock;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public char getPosition() {
        return this.position;
    }

    public KillerGame getKillerGame() {
        return this.killerGame;
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    private void processMessage(BufferedReader in, PrintWriter out) {
        String line;
        Boolean done = false;

        try {
            while (!done) {
                System.out.println("VH: Waiting for reading lines...");
                line = in.readLine();
                
                System.out.println("VH: line>>> " + line);

                //si line null es que el cliente ha cerrado/perdido la conexion
                if (line == null) {
                    done = true;

                } else {
                    String[] lines = line.trim().split("&");
                    switch (lines[0]) {

                        case "bye":
                            done = true;
                            break;

                        case "ball":
                            //o recibe el obj bola o cada parametro x separado
                            //cambiar coordenadas hardcodeadas
//                            this.killerGame.createAlive(new Ball(this.killerGame, 5, 5, 50, 50));
                            System.out.println("VH: recivdo ball");
                            new Ball(this.killerGame, 1, 1, 300, 300);
                            break;

                        default:
                            System.out.println("Client msg (default): " + line);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("PK: " + e);
        }
    }

    @Override
    public void run() {
        System.out.println("VH: Ha entrado en el run");

        try {
            // Get I/O streams from the socket. 
            System.out.println("VH run try socket: " + this.socket.toString());
            this.in = new BufferedReader(new InputStreamReader(
                    this.socket.getInputStream()));
            System.out.println("VH run try out>> " + this.out.toString());
            this.out = new PrintWriter(this.socket.getOutputStream(), true);

            

            // interact with a client
            this.processMessage(this.in, this.out);

            // Close client connection
            this.socket.close();

            System.out.println("User " + this.clientAddr + " disconected");

        } catch (Exception ex) {
            System.err.println("PK: kk");
            System.err.println("PK: " + ex);
        }

    }

    void startClient(String ip, int port) {

        this.ip = ip;
        this.port = port;

        new Thread(this.killerClient).start();
    }

}
