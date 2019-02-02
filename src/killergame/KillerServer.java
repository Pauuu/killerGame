/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
public class KillerServer implements Runnable {

    KillerGame killerGame;
    private Socket clientSock;
    private String clientAddr;

    public KillerServer(KillerGame kg) {
        this.killerGame = kg;

    }

    public void inicio() {
        try {
//            la ip es la suya misma
            ServerSocket serverSock = new ServerSocket(12345);

//            socket por el cual se comunicará el server con el cliente (?)
            Socket clientSock;
            String cliAddr;

            while (true) {

                System.out.println("Waiting for a client...");

                //crea un nuevo socket para el cliente que ha "entrado" 
                //del "server socket" gracias al metodo accept();
                clientSock = serverSock.accept();

//                imprime la ip del cliente
                System.out.println("Client connection from "
                        + clientSock.getInetAddress().getHostAddress());

                //guarda la direccion del cliente
                cliAddr = clientSock.getInetAddress().getHostAddress();

                //crea un nuevo socket con un nuevo hilo de ejecucion pasandole
                //la direccion del cliente y del nuevo socket <clientSock>
                new Thread(new KillerServerHandler(
                        this.killerGame,
                        clientSock,
                        cliAddr)
                ).start();

////               
            }

        } catch (Exception e) {
            System.out.println("test" + e);
        }
    }

    @Override
    public void run() {
        this.inicio();
    }

}
