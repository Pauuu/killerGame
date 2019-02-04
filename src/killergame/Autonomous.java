package killergame;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Autonomous extends Alive {

    public Autonomous(KillerGame kGame, int width, int height) {
        super(kGame, width, height);
        this.setImagen("imgs/cocinero.png");
    }

    @Override
    public void run() {
        while (true) {
            this.moveX(1);
            this.moveY(2);
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
