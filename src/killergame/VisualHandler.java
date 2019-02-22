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

    private KillerGame killerGame;
    private KillerClient2 killerClient;
    private Socket socket = null;
    private String clientAddr;

    private PrintWriter out;

    public VisualHandler(KillerGame kg) {
        this.killerGame = kg;
        this.killerClient = new KillerClient2(this, this.socket, "localhost", this.out);
//        this.clientSocket = cliSock;
//        this.clientAddr = cliAddr;

        this.tryConnection();
            
        

    }

    /**
     * crea conexion con un server gracias al cliente creado
     */
    public void tryConnection() {
        
        new Thread(this.killerClient).start();
    }
    
    /**
     * --sin acabar--
     */
    private void createFrame(){
        JFrame jf = new JFrame("IP");
        
        JButton ok = new JButton("OK");
        JTextField textField = new JTextField(40);
        
        
        jf.setSize(300, 100);
        
        jf.add(ok);
        jf.add(textField);
        
        jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public void setClientAddress(String cliAddr) {
        this.clientAddr = cliAddr;
    }
    
    public void sendBall(){
        this.out.println("ball");
    }
    
    
    /**
     * Comprobar si funciona
     * @param msj 
     */
    public void sendMessage(String msj){
        this.out.println(msj);
    }

    public void setSocket(Socket cliSock) {
        this.socket = cliSock;
    }

    public Socket getSocket() {
        return this.socket;
    }

    private void processMessage(BufferedReader in, PrintWriter out) {
        String line;
        Boolean done = false;

        try {
            while (!done) {
                System.out.println("VH: Waiting for reading lines...");
                line = in.readLine();

                //si line null es que el cliente ha cerrado/perdido la conexion
                if (line == null) {
                    done = true;

                } else {
                    String[] lines = line.trim().split(">>");
                    switch (lines[0]) {

                        case "bye":
                            done = true;
                            break;

                        case "ball":
                            //o recibe el obj bola o cada parametro x separado
                            //cambiar coordenadas hardcodeadas
//                            this.killerGame.createAlive(new Ball(this.killerGame, 5, 5, 50, 50));
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

        BufferedReader in;
        PrintWriter out;

        try {
            // Get I/O streams from the socket
            in = new BufferedReader(new InputStreamReader(
                    this.socket.getInputStream()));

            out = new PrintWriter(this.socket.getOutputStream(), true);


            // interact with a client
            this.processMessage(in, out);

            // Close client connection
            this.socket.close();

            System.out.println("User " + this.clientAddr + " disconected");

        } catch (Exception ex) {
            System.err.println("PK: kk");
            System.err.println("PK: " + ex);
        }

    }

}
