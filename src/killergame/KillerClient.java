package killergame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
public class KillerClient implements Runnable {

    private VisualHandler vh;
    private char position;
    private String clienAddress;

    public KillerClient(VisualHandler vh, char position) {
        this.vh = vh;
        this.position = this.vh.getPosition();
    }

    @Override
    public void run() {

        PrintWriter out;
        Socket cliSock;

        // bucle infinito
        while (true) {

            // mirar si hay conexion (socket) o no en su visual handler
            if (this.vh.getSocket() == null // mirar si no tiene socket
                    && vh.getIp() != null // mirar si tiene ip
                    && vh.getPort() != 0) { // mirar si tiene puerto

                try {
                    // crear nuevo socket
                    cliSock = new Socket(this.vh.getIp(), this.vh.getPort());

                    // enviar msj con datos
                    out = new PrintWriter(cliSock.getOutputStream(), true);
                    out.println("vm&" + this.vh.getPosition()+"&"+this.vh.getKillerGame().getKillerServer().getServerPort());
                    
                    this.vh.setConnection(cliSock);

                } catch (IOException ex) {
                    Logger.getLogger("KC: " + KillerClient.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("KC: no se ha podido establecer conexion");
                }
            }

            // dormir el hilo 0.2 seg
            try {
                Thread.sleep(2000);

            } catch (InterruptedException ex) {
                Logger.getLogger(KillerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
