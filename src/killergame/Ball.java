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
        this.setVelX(1);
        this.setVelY(7);

        //this.setImagen("imgs/blackBall.png");
    }

    //eliminar --> pasar a killer rules?
    private void comprobarColision() {

        //mira si colision en eje X
        if (this.posX >= this.killerGame.getFrameWidth() - this.witdh) {
            this.velX *= -1;

        } else if (this.posX <= 0) {
            this.velX *= -1;
        }

        //mira si colision en eje Y
        if (this.posY >= this.killerGame.getFrameHeight()- this.height) {
            this.velY *= -1;

        } else if (this.posY <= 0) {
            this.velY *= -1;
        }
    }

    @Override
    public void run() {
        while (true) {
            this.comprobarColision();
            this.moveX();
            this.moveY();
            try {
                Thread.sleep(9);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
