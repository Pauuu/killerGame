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

    public KillerPad(KillerGame kg, Socket cliSock, String ip, int port, String name) {
        this.killerGame = kg;
        this.clientSocket = cliSock;
        this.ip = ip;
        this.port = port;
        this.name = name;

        // añadir este objeto al array de killerpads del killer game
        this.killerGame.addKillerPad(this);

        // crear nueva killerShip
        new KillerShip(kg, 100, 100, 60, 60, 0, 0, this.ip, this.port, this.name);

        // iniciar hilo propio
        new Thread(this).start();
    }

    public static KillerShip ckeckKillerShip(KillerGame kg, String ip, int port) {

        for (int pos = 0; pos < kg.getVisibleObjects().size(); pos++) {

            // cerar en game metodo para obtener array de objs en especiico????
            // discriminar los killerShips
            if (kg.getVisibleObjects().get(pos) instanceof KillerShip) {

                // buscar el que tenga misma id
                if (((KillerShip) kg.getVisibleObjects().get(pos)).getIp().equalsIgnoreCase(ip)) {
                    return (KillerShip) kg.getVisibleObjects().get(pos);
                }
            }
        }

        return null;

    }

    private static boolean isMyMessage(String ipOrigen, int puertoOrigen, KillerGame kg) {
        // si:
        // ip distinta (AND puerto igual OR distinto)
        // OR
        // ip propia AND port distinto
        if ((!ipOrigen.equalsIgnoreCase(kg.getKillerServer().getIp()))
                || (ipOrigen.equalsIgnoreCase(kg.getKillerServer().getIp())
                && (puertoOrigen != kg.getKillerServer().getServerPort()))) {
            return false;
        }

        return true;

    }

    public static void doAction(String action, KillerShip kShip) {
        System.out.println("KP: " + action);

        // ordenar a la nave que se mueva
        switch (action) {

            case ("right"):

                kShip.setVelX(4);
                kShip.setVelY(0);
                break;

            case ("down"):

                kShip.setVelX(0);
                kShip.setVelY(4);
                break;

            case ("up"):

                kShip.setVelX(0);
                kShip.setVelY(-4);
                break;

            case ("left"):

                kShip.setVelX(-4);
                kShip.setVelY(0);
                break;

            case ("upright"):

                kShip.setVelX(4);
                kShip.setVelY(-4);
                break;

            case ("upleft"):

                kShip.setVelX(-4);
                kShip.setVelY(-4);
                break;

            case ("downright"):

                kShip.setVelX(4);
                kShip.setVelY(4);
                break;

            case ("downleft"):

                kShip.setVelX(-4);
                kShip.setVelY(4);
                break;

            case ("idle"):

                kShip.setVelX(0);
                kShip.setVelY(0);
                break;

            case ("shoot"):

                kShip.fire();
                break;

            case ("bbbye"):

                break;
        }

        System.out.println("nave NO null");

    }

    public static void sendRelayMessage(String line, KillerGame kg) {
        System.out.println("VH: nave null \n");
        kg.getKillerRight().sendMessage(
                line
        );
    }

    public static void manageAction(String line, KillerGame kg) {
        String[] message = line.trim().split("&");

        String action = message[3];
        String ipOrigen = message[4];
        int puertoOrigen = Integer.parseInt(message[5]);

        // comprobar si el msj ha dado toda la vuelta
        if (KillerPad.isMyMessage(ipOrigen, puertoOrigen, kg)) {

            KillerShip kShip = kg.checkKillerShip(
                    message[2], // ip nave
                    puertoOrigen // puerto
            );

            // comprobar si existe la nave
            if (kShip != null) {

                KillerPad.doAction(action, kShip);

//                System.out.println("KP: " + action);
//
//                // ordenar a la nave que se mueva
//                switch (action) {
//
//                    case ("right"):
//
//                        kShip.setVelX(4);
//                        kShip.setVelY(0);
//                        break;
//
//                    case ("down"):
//
//                        kShip.setVelX(0);
//                        kShip.setVelY(4);
//                        break;
//
//                    case ("up"):
//
//                        kShip.setVelX(0);
//                        kShip.setVelY(-4);
//                        break;
//
//                    case ("left"):
//
//                        kShip.setVelX(-4);
//                        kShip.setVelY(0);
//                        break;
//
//                    case ("upright"):
//
//                        kShip.setVelX(4);
//                        kShip.setVelY(-4);
//                        break;
//
//                    case ("upleft"):
//
//                        kShip.setVelX(-4);
//                        kShip.setVelY(-4);
//                        break;
//
//                    case ("downright"):
//
//                        kShip.setVelX(4);
//                        kShip.setVelY(4);
//                        break;
//
//                    case ("downleft"):
//
//                        kShip.setVelX(-4);
//                        kShip.setVelY(4);
//                        break;
//
//                    case ("idle"):
//
//                        kShip.setVelX(0);
//                        kShip.setVelY(0);
//                        break;
//
//                    case ("shoot"):
//
//                        kShip.fire();
//                        break;
//
//                    case ("bbbye"):
//
//                        break;
//                }
//
//                System.out.println("nave NO null");
            } else {

            }
            
            KillerPad.sendRelayMessage(line, kg);

//            System.out.println("VH: nave null \n");
//            kg.getKillerRight().sendMessage(
//                    line
//            );
        }

    }

    private void processClient(BufferedReader in, PrintWriter out) throws IOException {

        boolean closed = false;
        String line;
        String[] message;

        //mientras no haya msj de adios o conexion perdida
        while (!closed) {

            // si no existe, enviar msj a VH derecha
            // si existe, ejecutar comnando
            try {
                line = in.readLine(); //leer mensaje

                KillerPad.manageAction(line, this.killerGame);

                // comprobar si existe la killerShip en el game
//                KillerShip ks = this.killerGame.checkKillerShip(this.ip, this.port);
//
//                if (ks != null) {   // si la nave existe
//
//                    System.out.println("KP: messaje: " + action);
////                    ks.doAction(action);
//
//                } else {            // si la nave no existe   
//
//                    // enviar msj a modulo derecho
//                    // decir que cosas ha de hacer la bola en otro modulo visual
//                    this.killerGame.getKillerRight().sendMessage(
//                            this.killerGame.getKillerRight().getPosition()
//                            + "&" + "cks"
//                            + "&" + this.ip
//                            + "&" + action // comando
//                            + "&" + this.killerGame.getKillerServer().getIp()
//                            + "&" + this.killerGame.getKillerServer().getServerPort()
//                    );
////                    
//
//                    System.out.println("la nave no existe");
//                }
            } catch (IOException ex) {
                Logger.getLogger(KillerPad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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

        } catch (IOException e) {
            Logger.getLogger(KillerPad.class.getName()).log(Level.SEVERE, null, e);
        }

        System.out.println("NK: Client connection closed\n");
    }

}
