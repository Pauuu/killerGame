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

    private boolean dead = false;

    public Ball(KillerGame kGame, 
            double posX, double posY, 
            int width, int height,
            double velX, double velY
    ) {
        
        
        super(kGame, posX, posY, width, height, velX, velY);
        
        

        //this.setImagen("imgs/blackBall.png");
    }

    public void disparo() {
        new Thread(new Bullet(this.killerGame, this)).start();
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.ORANGE);
//        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);
        g2d.fillArc((int) this.posX, (int) this.posY, this.width, this.height, 30, 300);

    }
}
