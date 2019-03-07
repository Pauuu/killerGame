/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author pau
 */
public class KillerServer implements Runnable {

    private KillerGame killerGame;
    private ServerSocket serverSocket;
    private String ip;
    private int port;

    public KillerServer(KillerGame kg) {
        this.killerGame = kg;
        this.port = 8000;

    }

    public void escuchar() {
        Socket cliSock;
        String cliAddr;

        try {
           
            this.configurePort();
           
            while (true) {

                System.out.println("KS: Waiting for a client...");

                // crea un nuevo socket para el cliente que ha "entrado" 
                // del "server socket" gracias al metodo accept();
                cliSock = this.serverSocket.accept();

                //  imprime la ip del cliente
                System.out.println("KS: Client connection from "
                        + cliSock.getInetAddress().getHostAddress());

                //guarda la direccion del cliente
                cliAddr = cliSock.getInetAddress().getHostAddress();

                this.createConnectionHandler(cliSock, cliAddr);

//                this.crearConexiones(cliSock, cliAddr);
                // mira si null, crea conexiones.Primero izq, luego derecha
//                this.crearConexiones(cliSock, cliAddr);
            }

        } catch (Exception e) {
            System.err.println("test" + e);
        }
    }

    public void configurePort() {

        while (this.serverSocket == null) {

            try {
                this.serverSocket = new ServerSocket(this.port);
                this.ip = InetAddress.getLocalHost().getHostAddress();

            } catch (IOException ex) {
                this.port++;
                System.out.println("valor del puerto aumentado");
            }
        }
    }

    public KillerGame getKillerGame() {
        return this.killerGame;
    }

    private void createConnectionHandler(Socket cliSock, String cliAddr) {
        ConectionHandler ch = new ConectionHandler(this, cliSock, cliAddr);
        new Thread(ch).start();
    }

    @Override
    public void run() {

        this.escuchar();
    }

    public int getServerPort() {
        return this.port;
    }
    
     public String getIp(){
        return this.ip;
    }

}
