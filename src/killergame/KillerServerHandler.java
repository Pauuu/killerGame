package killergame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KillerServerHandler implements Runnable {

    public KillerGame killerGame;
    private Socket clientSocket;
    private String clientAddress;

    public KillerServerHandler(KillerGame kGame, Socket cliSocket, String cliAddr) {
        this.killerGame = kGame;
        this.clientSocket = cliSocket;
        this.clientAddress = cliAddr;
    }

    private boolean comprobarSiModuloVisual() {
        return true; //añadir comprobacion si modulo
    }

    private PreviousKiller createPreviousKiller() {
        PreviousKiller pk = new PreviousKiller(
                this.killerGame,
                this.clientSocket,
                this.clientAddress);

        new Thread(pk).start();

        this.killerGame.setPreviousKiller(pk);

        System.out.println("KSH: thread Previous Killer started");

        return pk;
    }

    private NextKiller createNextKiller() {
        NextKiller nk = new NextKiller(
                this.killerGame,
                this.clientSocket,
                this.clientAddress);

        new Thread(nk).start();

        this.killerGame.setNextKiller(nk);

        System.out.println("KSH: thread Next Killer started");

        return nk;
    }

    private void processClient() {

        try {
            if (this.comprobarSiModuloVisual()) { //de momento siempre verdadero

                //cambiar comprobacion
                boolean success;

                success = this.killerGame.tryConnectPreviousKiller(
                        this.clientSocket,
                        this.clientAddress);

                if (!success) {
                    success = this.killerGame.tryConnectNextKiller(
                            this.clientSocket,
                            this.clientAddress);
                }

                if (!success) {    //si no se cumple ninguna condicion
                    System.err.println("KSH: Address: " + this.clientAddress
                            + " no se ha llegado a conectar");
                }

            }

        } catch (Exception e) {
            System.err.println("KSH: algo ha fallado");
            System.err.println("KSH: " + e);
        }
    }

    @Override
    public void run() {

        try {

            this.processClient();

            // Close client connection
        } catch (Exception ex) {
            System.err.println("KSH: kk");
            System.err.println("KSH: " + ex);

        }

    }

}
