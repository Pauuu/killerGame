/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

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
    private char position;
    private KillerGame killerGame;
    private KillerClient killerClient;
    private PrintWriter out;
    private Socket socket = null;
    private String clientAddr;
    private String ip;
    private int serverPort;
    private int port;

    public VisualHandler(KillerGame kg, char position) {
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

    /**
     * Comprobar si funciona
     *
     * @param msj
     */
    public void sendMessage(String msj) {
        this.out.println(msj);
    }

    private void processMessage(BufferedReader in, PrintWriter out) {
        String line;
        Boolean done = false;

        try {
            while (!done) {
                System.out.println("VH: Waiting for reading lines...");
                System.out.println("DEBUG >> socket string: " + this.socket.toString());
                line = in.readLine();

                System.out.println("VH: mensaje recibido: " + line);

                // si line null es que el cliente ha cerrado/perdido la conexion
                if (line == null) {
                    System.err.println("debugkk");
                    done = true;
                    this.socket = null;

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
        } catch (Exception ex) {
            Logger.getLogger("VH: " + VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("VH: " + ex);
//                this.socket.close();
            this.socket = null;
        }
    }

    @Override
    public void run() {
        this.processMessage(this.in, this.out);
    }

    public char getPosition() {
        return this.position;
    }

    public KillerGame getKillerGame() {
        return this.killerGame;
    }

    public synchronized Socket getSocket() {
        return this.socket;
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public synchronized void startConnection(Socket cliSock, int serverPort) {
        if (this.getSocket() == null) {

            try {
                // setear socket
                this.socket = cliSock;

                // setear PrintWriter (poder enviar msjs)
                this.out = new PrintWriter(this.socket.getOutputStream(), true);

                // setar BufferedReader (poder recibir msjs)
                this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

                // setear port del servidor
                this.serverPort = serverPort;
                System.out.println("DEBUG >> VH: Server port: " + this.serverPort);
                System.out.println("DEBUG >> VH: port: " + this.port);

                System.out.println("VH: toda conexion ok?");
                System.out.println("VH: Iniciando run de de VM");

                new Thread(this).start();

            } catch (IOException ex) {
                // excepcion al no poder(?) leer
                Logger.getLogger("VH: " + VisualHandler.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else {
            System.out.println("VH: no se ha seteado el socket");

            // 
        }
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public int getServerPort() {
        return this.serverPort;
    }

}
