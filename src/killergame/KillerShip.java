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
public class KillerShip extends Controlled {

    // ip del mando de origen
    private String ip;

    // nombre de la nave dado por el usuario
    private String name;

    // puerto del mando de origen (x si juego en local)
    private int port;

    public KillerShip(KillerGame kGame,
            int posX,
            int posY,
            int width,
            int height,
            String killerPadIp,
            int port,
            String name) {
        super(kGame, posX, posY, width, height);

        this.velX = 4d;

    }

    @Override
    public void render(Graphics2D g2d) {

        g2d.setColor(Color.green);
        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);
    }

}
