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

    private KillerGame killerGame;

    public KillerServer(KillerGame kg) {
        this.killerGame = kg;

    }

    public void abrirConexion() {
        ServerSocket serverSock;    // repasar pq serverSocket
        Socket cliSock;
        String cliAddr;

        try {
            // la ip es la suya misma, port 8000
            serverSock = new ServerSocket(8000);

            // socket por el cual recibirá al cliente
            // de momento no discrimina si es un killer pad o no //lo hace el ch
            while (true) {

                System.out.println("KS: Waiting for a client...");

                // crea un nuevo socket para el cliente que ha "entrado" 
                // del "server socket" gracias al metodo accept();
                // --repasar que hace este metodo--
                cliSock = serverSock.accept();

                //  imprime la ip del cliente
                System.out.println("KS: Client connection from "
                        + cliSock.getInetAddress().getHostAddress());

                //guarda la direccion del cliente
                cliAddr = cliSock.getInetAddress().getHostAddress();
                
                

                this.gestionarConexion(cliSock, cliAddr);

//                this.crearConexiones(cliSock, cliAddr);
                // mira si null, crea conexiones.Primero izq, luego derecha
//                this.crearConexiones(cliSock, cliAddr);
            }

        } catch (Exception e) {
            System.err.println("test" + e);
        }
    }

     private void gestionarConexion(Socket cliSock, String cliAddr) {
        ConectionHandler ch = new ConectionHandler(this.killerGame, cliSock, cliAddr);
        new Thread(ch).start();
    }

    @Override
    public void run() {
        this.abrirConexion();
    }

   

}
