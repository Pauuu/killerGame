/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
public class Ball extends Autonomous {

    public Ball(KillerGame kGame, int width, int height) {
        super(kGame, width, height);

        //this.setImagen("imgs/blackBall.png");
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);

    }

    @Override
    public void run() {

        long timeDiffNano;      //direfencia de tiempo actual menos anterior
        long previousTimeNano;  //timepo anterior

        previousTimeNano = System.nanoTime();

        this.velX = 1;
        this.velY = 1;

        while (true) {

            timeDiffNano = System.nanoTime() - previousTimeNano;

            long dist = this.velX * timeDiffNano;

            this.testColision();
            this.moveX((int) (this.velX));
            this.moveY((int) (this.velY));
            this.hitBox.setLocation(posX, posY);
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
