package killergame;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Autonomous extends Alive {

    public Autonomous(KillerGame kGame) {
        super(kGame);
        this.setImagen("imgs/cocinero.png");
    }

    @Override
    public void run() {
        while (true) {
//        this.setVelX(10);
            this.moveX(1);
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
