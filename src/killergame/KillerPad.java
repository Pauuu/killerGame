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
public class KillerPad implements Runnable {

    private KillerGame killerGame;
    private Socket clientSocket;
    private String clientAddress;
    private Player player;

    public KillerPad(KillerGame kg, Socket cliSock, String cliAddr) {
        this.killerGame = kg;
        this.clientSocket = cliSock;
        this.clientAddress = cliAddr;
    }

    private void processClient(BufferedReader in, PrintWriter out) {

        boolean closed = false;
        String uglyMessaje;
        String[] messaje;

        //mientras no haya msj de adios o conexion perdida
        while (!closed) {
            try {
                uglyMessaje = in.readLine(); //leer mensaje

                System.out.println("KP: messaje: " + uglyMessaje);

                messaje = uglyMessaje.trim().split(">>");

                switch (messaje[0]) {
                    case ("right"):
                        this.player.moveY(0);
                        this.player.moveX(1);
                        break;
                    case ("down"):
                        this.player.moveX(0);
                        this.player.moveY(1);
                        break;
                    case ("up"):
                        this.player.moveX(0);
                        this.player.moveY(-1);
                        break;
                    case ("left"):
                        this.player.moveY(0);
                        this.player.moveX(-1);
                        break;
                    case ("idle"):
                        this.player.moveX(0);
                        this.player.moveY(0);
                    case ("bbbye"):
                        
                        
                        break;
                }

            } catch (IOException ex) {
                Logger.getLogger(KillerPad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void startNave(int x, int y) {
        this.player = new Player(this.killerGame, 20, 20);
        this.killerGame.createAlive(player, x, y);
        new Thread(this.player).start();
    }

    @Override
    public void run() {
        BufferedReader in;
        PrintWriter out;

        try {
            // Get I/O streams from the socket
            in = new BufferedReader(new InputStreamReader(
                    this.clientSocket.getInputStream()));

            out = new PrintWriter(this.clientSocket.getOutputStream(), true);

            this.startNave(40, 40);

            // interact with a client
            this.processClient(in, out);

            // Close client connection
            this.clientSocket.close();

        } catch (Exception ex) {
            System.err.println("NK: kk");
        }

        System.out.println("NK: Client connection closed\n");
    }

}
