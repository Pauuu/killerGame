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
public class VisualHandler implements Runnable {

    private KillerGame killerGame;
    private Socket clientSocket;
    private String clientAddr;

    public VisualHandler(KillerGame kg, Socket cliSock, String cliAddr) {
        this.killerGame = kg;
        this.clientSocket = cliSock;
        this.clientAddr = cliAddr;
    }

    private void processMessage(BufferedReader in, PrintWriter out) {
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
        System.out.println("lkasdjfñlkasjfd");
        
        BufferedReader in;
        PrintWriter out;

        try {
            // Get I/O streams from the socket
            in = new BufferedReader(new InputStreamReader(
                    this.clientSocket.getInputStream()));

            out = new PrintWriter(this.clientSocket.getOutputStream(), true);

            // interact with a client
            this.processMessage(in, out);

            // Close client connection
            this.clientSocket.close();
            
            System.out.println("User " + this.clientAddr + " disconected");

        } catch (Exception ex) {
            System.err.println("PK: kk");
            System.err.println("PK: " + ex);
        }

    }

}
