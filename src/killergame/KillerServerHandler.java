package killergame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public class KillerServerHandler implements Runnable {

    public KillerGame killerGame;
    private Socket clientSock;
    private String clientAddr;

//    private BufferedReader in;
//    private PrintWriter out;
    public KillerServerHandler(KillerGame kGame, Socket cliSocket, String cliAddr) {
        this.killerGame = kGame;
        this.clientSock = cliSocket;
        this.clientAddr = cliAddr;

    }

    private void processClient(BufferedReader in, PrintWriter out) {
        String line;
        Boolean done = false;

        try {
            while (!done) {
                System.out.println("read line()");
                line = in.readLine();

                //si line null es que el cliente ha cerrado/perdido la conexion
                if (line == null) {
                    done = true;

                } else {
                    switch (line.trim()) {

                        case "bye":
                            done = true;
                            break;

                        case "ball":
                            this.startBola();

                        default:
                            System.out.println("Client msg: " + line);
                            this.doRequest(line, out);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("algo ha fallado");
        }
    }

    private void startBola() {
        System.out.println("ball");
        Autonomous bola = new Autonomous(this.killerGame);
        bola.setPosX(9);
        bola.setPosY(4);
        this.killerGame.addAutonomousObj(bola);
        new Thread(bola).start();
    }

    private void doRequest(String line, PrintWriter out) {
        if (line.trim().toLowerCase().equals("get")) {

            System.out.println("Processing 'get'");
            out.print("");

        } else {
            System.out.println("Ignoring input line");
            out.println("debug: " + line);
        }
    }

    @Override
    public void run() {
        BufferedReader in;
        PrintWriter out;

        try {
            // Get I/O streams from the socket
            in = new BufferedReader(new InputStreamReader(
                    this.clientSock.getInputStream()));

            out = new PrintWriter(this.clientSock.getOutputStream(), true);

            // interact with a client
            this.processClient(in, out);

            // Close client connection
            this.clientSock.close();

        } catch (Exception ex) {
            System.err.println("kk");
        }

        System.out.println("Client connection closed\n");

        //lo vuelve a imprimir desde esta clase
        System.out.println("Waiting for a client...\n");

        // backup scores after client finish
        //this.hs.saveScores();
    }

}
