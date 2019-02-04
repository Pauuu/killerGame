/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
public class Ball extends Autonomous {

    public Ball(KillerGame kGame, int width, int height) {
        super(kGame, width, height);
        
        this.setImagen("imgs/cocinero.png");

    }

    @Override
    public void run() {
        while (true) {
            this.moveX();
            this.moveY();
            try {
                Thread.sleep(18);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
