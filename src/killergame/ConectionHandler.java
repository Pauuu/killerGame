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
    private Socket clientSocket;
    private String clientAddress;

    public ConectionHandler(KillerGame kg, Socket cliSock, String cliAddr) {

        this.killerGame = kg;
        this.clientSocket = cliSock;
        this.clientAddress = cliAddr;

    }

    private void discriminarModulo() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            String tipoModulo = in.readLine();

            switch (tipoModulo) {
                case "vm":

                    // hacer que lo compruebe el visual handler (?)
                    
                    //cambiar comprobacion
                    boolean success = this.killerGame.tryConnectLeftKiller(
                            this.clientSocket,
                            this.clientAddress);
                    
                    if (success) {
                        System.out.println("izquierda");
                    }

                    if (!success) {
                        success = killerGame.tryConnectRightKiller(
                                this.clientSocket,
                                this.clientAddress);
                        System.out.println("derecha");
                    }

                    if (!success) {
                        System.out.println("no se ha podido conectar");
                    }

                    break;
                    
                case "kp":
                    
                    this.killerGame.connectKillerPad(this.clientSocket, this.clientAddress);
                    System.out.println("killerpad");
                    break;
                    
                default:
                    System.out.println("default");
                    break;
            }

        } catch (IOException ex) {
            Logger.getLogger(ConectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void startConectionHandler(KillerGame kg) {

    }

    @Override
    public void run() {
        this.discriminarModulo();
    }

}
