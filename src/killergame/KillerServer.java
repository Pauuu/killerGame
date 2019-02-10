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

    public void inicioEsperaConexion() {
        try {
//            la ip es la suya misma
            ServerSocket serverSock = new ServerSocket(30123);

//            socket por el cual recibirá al cliente
            Socket cliSock;
            String cliAddr;

            //de momento no discrimina si es un killer pad o no
            while (true) {

                System.out.println("KS: Waiting for a client...");

                //crea un nuevo socket para el cliente que ha "entrado" 
                //del "server socket" gracias al metodo accept();
                cliSock = serverSock.accept();

//                imprime la ip del cliente
                System.out.println("KS: Client connection from "
                        + cliSock.getInetAddress().getHostAddress());

                //guarda la direccion del cliente
                cliAddr = cliSock.getInetAddress().getHostAddress();

                this.crearConexiones(cliSock, cliAddr);

                //mira si null, crea conexiones.Primero izq, luego derecha
//                this.crearConexiones(cliSock, cliAddr);
            }

        } catch (Exception e) {
            System.out.println("test" + e);
        }
    }

    private void crearConexiones(Socket cliSock, String cliAddr) {
        new Thread(new KillerServerHandler(
                this.killerGame, 
                cliSock, 
                cliAddr)
        ).start();
    }

    @Override
    public void run() {
        this.inicioEsperaConexion();
    }

}
