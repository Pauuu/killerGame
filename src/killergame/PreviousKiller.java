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

/**
 *
 * @author pau
 */
public class PreviousKiller implements Runnable {

    private KillerGame killerGame;
    private Socket clientSock;
    private String clientAddr;

    public PreviousKiller(KillerGame kGame, Socket cliSocket, String cliAddress) {
        this.killerGame = kGame;
        this.clientSock = cliSocket;
        this.clientAddr = cliAddress;
    }

    private void processClient(BufferedReader in, PrintWriter out) {
        String line;
        Boolean done = false;

        try {
            while (!done) {
                System.out.println("PK: Waiting for reading lines...");
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
                            Autonomous ball = new Autonomous(this.killerGame, 5, 10);
                            ball.setPosX(Integer.parseInt(lines[1]));
                            ball.setPosY(Integer.parseInt(lines[2]) + 100);
                            this.startBola(ball);
                            break;

                        default:
                            System.out.println("Client msg (default): " + line);
                            this.doRequest(line, out);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("PK: " + e);
        }
    }

    private void startBola(Autonomous aObj) {
        this.killerGame.addAutonomousObj(aObj);
        new Thread(aObj).start();
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
            System.err.println("PK: kk");
            System.err.println("PK: " + ex);
        }

    }

}
