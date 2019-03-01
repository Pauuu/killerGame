/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
// cerrar sockets si no hay comunicacion
public class ConectionHandler implements Runnable {

    private KillerGame killerGame;
    private KillerServer killerServer;
    private Socket clientSocket;
    private String clientAddress;

    public ConectionHandler(KillerServer ks, Socket cliSock, String cliAddr) {

        this.killerServer = ks;
        this.clientSocket = cliSock;
        this.clientAddress = cliAddr;

        this.killerGame = this.killerServer.getKillerGame();

    }

    private void discriminarModuloVisual(String[] peticion) {

        if (peticion[1].equalsIgnoreCase("r")) {
            this.killerGame.getVisualHandler('r').setKiller(clientSocket, clientAddress);
            System.out.println("CH: conexion recibida >> " + peticion[1]);

        } else if (peticion[1].equalsIgnoreCase("l")) {
            this.killerGame.getVisualHandler('l').setKiller(clientSocket, clientAddress);
            System.out.println("CH: conexion recibida >> " + peticion[1]);

        } else {
            System.out.println("CH: ignorando msjs -> " + peticion[1]);
        }
    }

    private void gestionarPeticion() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            String peticion[] = in.readLine().trim().split("&");
            
            System.out.println("CH: peticion[0] " + peticion[0]);

            if (peticion[0].equalsIgnoreCase("vm")) {
                // gestionar si es un moduo visual
                this.discriminarModuloVisual(peticion);
                System.out.println("CH: conexion recibida >> VM");

            } else if (peticion[0].equalsIgnoreCase("kp")) {
                // gestionar conexion de Killer Pad

            } else {
                // ignorsr la peticion
                System.out.println("CH: Se ha ignorado la peticion: " + peticion[0]);
            }

        } catch (IOException ex) {
            Logger.getLogger(ConectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        this.gestionarPeticion();
    }

}
