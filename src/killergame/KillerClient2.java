package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
public class KillerClient2 implements Runnable {

    private VisualHandler visualHandler;
    private Socket socket;
    private String clienAddress;
    private String ip;
    private PrintWriter out;

    public KillerClient2(VisualHandler vh, Socket sock, String ip, PrintWriter out) {
        this.visualHandler = vh;
        this.socket = sock;
        this.ip = ip;
        this.out = out;
    }

    public boolean makeContact() {
        try {
            this.socket = new Socket(this.ip, 8000);
            this.clienAddress = this.socket.getInetAddress().getHostAddress();

            try {
                this.out = new PrintWriter(this.socket.getOutputStream(), true);

                Thread.sleep(300);
                this.out.println("vm");
                System.out.println("KC2: mensaje 'vm' enviado");
                

            } catch (IOException ex) {
                Logger.getLogger(VisualHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("KC2: devuelto 'true'");
            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public void run() {
        boolean connected = false;
        while (!connected) {
            connected = this.makeContact();
        }
    }

}
