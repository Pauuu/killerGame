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

    public Ball(KillerGame kGame, int posX, int posY, int width, int height) {
        super(kGame, posX, posY, width, height);
        this.velX = 4d;  //quitar?
        this.velY = 5d;  //quitar?
      

        //this.setImagen("imgs/blackBall.png");
    }
    
    public void disparo(){
        new Thread(new Bullet(this.killerGame, this)).start();
    }

    

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.ORANGE);
//        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);
        g2d.fillArc(this.posX, this.posY, this.witdh, this.height, 30, 300);


    }
}
