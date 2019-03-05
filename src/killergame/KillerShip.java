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
            double velX,
            double velY,
            String killerPadIp,
            int port,
            String name) {
        
        super(kGame, posX, posY, width, height, 0, 0);

        this.posX = posX;
        this.posY = posY;
        this.witdh = width;
        this.height = height;
        this.ip = killerPadIp;
        this.port = port;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void render(Graphics2D g2d) {

        g2d.setColor(Color.green);
        g2d.fillOval((int) this.posX, (int) this.posY, this.witdh, this.height);
    }

}
