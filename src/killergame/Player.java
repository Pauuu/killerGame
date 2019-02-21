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
public class Player extends Controlled {

    public Player(KillerGame kGame, int posX, int posY, int width, int height) {
        super(kGame, posX, posY, width, height);

    }

    @Override
    public void render(Graphics2D g2d) {
        //  g2d.drawImage(this.imagen, this.posX, this.posY, null);
        g2d.setColor(Color.green);
        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);
    }

    @Override
    public void run() {

        while (true) {
            this.testColision();
           // this.moveX(this.velX);
            this.moveY(this.velY);
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
