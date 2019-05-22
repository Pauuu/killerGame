/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergamePau;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pau
 */
public class Bullet extends Autonomous {

    private Alive origenBala;
    private int distancia = 0;

    public Bullet(KillerGame kGame, Alive aObj) {
        super(kGame, aObj.getPosX() - 30, aObj.getPosY() + aObj.getWidth() / 2, 15, 15, -4d, 0);
        // --cambiar--

        this.origenBala = aObj;
//        this.posX = 90;
//        this.posY = 90;
//        this.posY = aObj.posY;

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.fillOval((int) this.posX, (int) this.posY, this.width, this.height);
    }

    @Override
    public void run() {
        double posXorigin = this.posX;
        double distance = 0;
        

        while (distance < 500) {

            distance += Math.abs(this.posX - posXorigin);

            this.time = System.nanoTime();

            //actualiza la posicion del obj
            // -- calcular la diferencia de tiempo --
            this.updatePosition();
            this.updateHitBox(); //quitar? ---> ha de llamar actualizar cada vez?
            this.testColision();

//                System.out.println("timediff: " + timeDiffNano);
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.kill();
    }

}
