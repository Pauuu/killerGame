package killergame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public class KillerClient {

    private KillerGame killerGame;
    private static final int PORT = 12345;
    // server details
    private static final String HOST = "localhost";
    private Socket sock;
    private BufferedReader in;
    private PrintWriter out;

    public KillerClient(KillerGame kg) {
        this.killerGame = kg;
    }

    public void closeLink() {
        try {
            this.out.println("bye");
            // tell server
            this.sock.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.exit(0);
    }

    private void sendGet() {
        // Send "get" command, read response and display it
        // Response should be "HIGH$$ n1 & s1 & .... nN & sN & "
        try {
            this.out.println("get");
            System.out.println("esperando respuesta...");
            //espera y lee la respuesta del servidor
            String line = this.in.readLine();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void sendBola(Autonomous aObj) {
        this.out.println("ball--" + aObj.getPosX() + "--" + aObj.getPosY());
        System.out.println("test enviada bola?");
    }

    public void makeContact() {
        try {
            this.sock = new Socket(HOST, PORT);

            /*
             * puntero a un input stream (datos de llegada en crudo) __this.sock.getInputStream()__
             * "traduce la info cruda a texto(?)" __InputStreamReader()__
             * lo guarfa en un buffer reader __BufferedReader(<toda la mierda de arriba>)__
             * 
             */
            this.in = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));

            /*
             * puntero al stream que se enviara al servidor y en el que escribiremos
             */
            this.out = new PrintWriter(this.sock.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
