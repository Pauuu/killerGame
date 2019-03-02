/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author pau
 */
public class KillerServer implements Runnable {

    private KillerGame killerGame;
    private ServerSocket serverSocket;
    private int port = 8000;

    public KillerServer(KillerGame kg) {
        this.killerGame = kg;

    }

    public void abrirConexion() {
        Socket cliSock;
        String cliAddr;

        try {
            // la ip es la suya misma, port 8000
            this.configurePort();
            
            System.out.println("KS: puerto actual: " + this.port);
            // socket por el cual recibirá al cliente
            // de momento no discrimina si es un killer pad o no //lo hace el ch
            while (true) {

                System.out.println("KS: Waiting for a client...");

                // crea un nuevo socket para el cliente que ha "entrado" 
                // del "server socket" gracias al metodo accept();
                // --repasar que hace este metodo--
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
        
        this.abrirConexion();
    }
    
    public int getServerPort(){
        return this.port;
    }

}
