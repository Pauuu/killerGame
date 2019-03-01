package killergame;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
public class KillerClient implements Runnable {

    private VisualHandler visualHandler;
    private Socket socket;
    private char position;
    private String clienAddress;

    public KillerClient(VisualHandler vh, Socket sock, char position) {
        this.visualHandler = vh;
        this.position = this.visualHandler.getPosition();
        this.socket = sock;
    }

    public void tryContact(Socket sock) {
//        Socket vhSocket = this.visualHandler.getSocket();

        try {
            PrintWriter out = new PrintWriter(sock.getOutputStream());
            out = new PrintWriter(sock.getOutputStream(), true);

            out.println("vm&" + this.position);
            System.out.println("KC2: mensaje 'vm' enviado");

        } catch (Exception e) {
            System.out.println("KC: " + e);
        }
    }

    @Override
    public void run() {
        System.out.println("KC: entrado en el run");

        while (true) {
            try {
                Socket vhSocket = this.visualHandler.getSocket();
                
                if (vhSocket == null) {

                    vhSocket = new Socket(
                            this.visualHandler.getIp(),
                            this.visualHandler.getPort());

                    System.out.println("test");
                    System.out.println("KC while true socket:" + vhSocket.toString());
                    this.tryContact(vhSocket);

                    this.visualHandler.setSocket(vhSocket);
                }

            } catch (Exception ex) {
                System.out.println(ex);
            }

            // duerme el bucle
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(KillerClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
