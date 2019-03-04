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
//añadir metodos estaticos para interpretar x si algun kg no tiene kp
public class KillerPad implements Runnable {

    private KillerGame killerGame;
    private Socket clientSocket;
    private String ip;
    private String name;
    private KillerShip player;
    private int port;
    
    public KillerPad(KillerGame kg, Socket cliSock, String ip, int  port, String name) {
        this.killerGame = kg;
        this.clientSocket = cliSock;
        this.ip = ip;
        this.port = port;
        this.name = name;
        
        // añadir este objeto al array de killerpads del killer game
        this.killerGame.addKillerPad(this);
        
        // crear nueva killerShip
        new KillerShip(kg, 0, 0, 60, 60, this.ip, this.port, this.name);
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

    public static void createKillerShip(KillerGame kg, String ip, int port, String name) {

        // crear nueva killerShip
        new KillerShip(kg, 0, 0, 60, 60, ip, port, name);

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
