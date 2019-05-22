/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergamePau;

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

        String posicion;
        int clientPort;

        posicion = peticion[1];
        clientPort = Integer.parseInt(peticion[2]);

        if (!posicion.equalsIgnoreCase("l") && !posicion.equalsIgnoreCase("r")) {
            System.out.println("CH: ignorando msjs -> " + posicion);
            return; //====================== peticion ignorada ==============>>>
        }

        // inicia la conexion del visual handler
        // setea el socket al visual handler
        // cambia la posicion
        posicion = (posicion.equalsIgnoreCase("r") ? "l" : "r");
        
        
        this.killerGame.getVisualHandler(posicion).setConnection(this.clientSocket, clientPort);
//        this.killerGame.getVisualHandler(posicion).startConnection(this.clientSocket, serverPort);
        System.out.println("CH: conexion recibida -> " + posicion);
    }

    private void gestionarPeticion() {

        String shipName;
        String uglyMessage;
        String[] peticion;

        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(this.clientSocket.getInputStream()));

            // almacena el msj en crudo
            uglyMessage = in.readLine();

            // separa la info por lso caracteres "/"
            peticion = uglyMessage.split("&");

            System.out.println("CH: ugly message " + uglyMessage);

            if (peticion[0].equalsIgnoreCase("vm")) {

                // gestionar si es un moduo visual
                System.out.println("CH: conexion recibida >> VM");
                this.discriminarModuloVisual(peticion);

            } else if (peticion[0].equalsIgnoreCase("kp")) {
                // gestionar conexion de Killer Pad 
                // -- protocolo distinto al del killer pad --

            } else if (peticion[0].substring(0, 5).equalsIgnoreCase("fromP")) {
                // crear nuevo player desde el killerPad

                // --test--
                shipName = uglyMessage.substring(uglyMessage.lastIndexOf("fromP") + 1);

                new KillerPad(
                        this.killerGame,
                        this.clientSocket,
                        this.clientAddress,
                        this.killerServer.getServerPort(),
                        shipName
                );

            } else {
                // ignorar la peticion
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
