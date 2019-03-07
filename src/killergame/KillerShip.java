/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

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
            double posX,
            double posY,
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
        this.width = width;
        this.height = height;
        this.ip = killerPadIp;
        this.port = port;
        this.name = name;

        this.setVelX(velX);
        this.setVelY(velY);
    }

    public void doAction(String action) {
        System.out.println("KP: " + action);

        switch (action) {

            case ("right"):

                this.setVelX(4);
                this.setVelY(0);
                break;

            case ("down"):

                this.setVelX(0);
                this.setVelY(4);
                break;

            case ("up"):

                this.setVelX(0);
                this.setVelY(-4);
                break;

            case ("left"):

                this.setVelX(-4);
                this.setVelY(0);
                break;

            case ("upright"):

                this.setVelX(4);
                this.setVelY(-4);
                break;

            case ("upleft"):

                this.setVelX(-4);
                this.setVelY(-4);
                break;

            case ("downright"):

                this.setVelX(4);
                this.setVelY(4);
                break;

            case ("downleft"):

                this.setVelX(-4);
                this.setVelY(4);
                break;

            case ("idle"):

                this.setVelX(0);
                this.setVelY(0);
                break;

            case ("shoot"):
                this.fire();
                break;

            case ("bbbye"):

                break;
        }
    }

    private void fire() {
        new Thread(new Bullet(this.killerGame, this)).start();
    }

    protected void kill() {

        ArrayList<VisibleObject> listVisibleObjs;

        // puntero a la lista de visual objects del killerGame
        listVisibleObjs = this.killerGame.getVisibleObjects();

        for (int pos = 0; pos < listVisibleObjs.size(); pos++) {
            if (this.killerGame.getVisibleObjects().get(pos) == this) {
                
                this.alive = false;
                this.killerGame.getVisibleObjects().remove(pos);
            }
        }
        
        this.killerGame.sendDedNotification(this.ip, this.port);
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
        g2d.fillOval((int) this.posX, (int) this.posY, this.width, this.height);
    }

}
