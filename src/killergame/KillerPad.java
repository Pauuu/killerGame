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

    private BufferedReader in;
    private PrintWriter out;

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

    @Override
    public void run() {

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

    public static void manageMessage(String line, KillerGame kg) {
        String[] message = line.trim().split("&");
        String action = message[3];

        String[] ipPort = message[2].trim().split("/");
        String ip = ipPort[0];
        int port = Integer.parseInt(ipPort[1]);

        // mira si tiene el killerPad
        KillerPad kPad = kg.searchKillerPad(ip, port);

        // si KillerPad no existe
        if (kPad == null) {
            // si no tiene el killerPad
            kg.getKillerRight().sendMessage(line);
            return; //==================== killer Pad no existe =========>>>>
        }

        // si el mensaje es ded
        if (action.equalsIgnoreCase("ded")) {
            kPad.sendMessageToMobile(action);
        }

    }

    private void processClient(BufferedReader in, PrintWriter out) {

        boolean closed = false;
        String action;
        String[] message;

        //mientras no haya msj de adios o conexion perdida
        while (!closed) {

            // si no existe, enviar msj a VH derecha
            // si existe, ejecutar comnando
            try {
                action = in.readLine(); //leer mensaje

                // comprobar si existe la killerShip en el game
                KillerShip ks = this.killerGame.checkKillerShip(this.ip, this.port);

                if (ks != null) {   // si la nave existe

                    System.out.println("KP: messaje: " + action);
                    ks.doAction(action);

                } else {            // si la nave no existe   

                    // enviar msj a modulo derecho
                    // decir que cosas ha de hacer la bola en otro modulo visual
                    this.killerGame.getKillerRight().sendMessage(
                            this.killerGame.getKillerRight().getPosition()
                            + "&" + "cks"
                            + "&" + this.ip
                            + "&" + action // comando
                            + "&" + this.killerGame.getKillerServer().getIp()
                            + "&" + this.killerGame.getKillerServer().getServerPort()
                    );
//                    

                    System.out.println("la nave no existe");
                }

            } catch (IOException ex) {
                Logger.getLogger(KillerPad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public KillerGame getKillerGame() {
        return killerGame;
    }

    public void setKillerGame(KillerGame killerGame) {
        this.killerGame = killerGame;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KillerShip getPlayer() {
        return player;
    }

    public void setPlayer(KillerShip player) {
        this.player = player;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    void sendMessageToMobile(String message) {
        this.out.println(message);
    }

}
