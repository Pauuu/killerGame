/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author pau
 */
public class Player extends Controlled {

    public Player(KillerGame kGame, int posX, int posY, int width, int height) {
        super(kGame, posX, posY, width, height);

        this.velX = 4d;

    }

    @Override
    public void render(Graphics2D g2d) {

        g2d.setColor(Color.green);
        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);
    }

   

//    }
}
