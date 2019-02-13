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
    Color c;

    public Ball(KillerGame kGame, int width, int height) {
        super(kGame, width, height);
        this.velX = 1;  //quitar?
        this.velY = 1;  //quitar?

        //this.setImagen("imgs/blackBall.png");
    }
    
    public void setColor(Color c){
        this.c = c;
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(c);
        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);

    }

    @Override
    public void run() {

        //direfencia de tiempo actual menos anterior
        double timeDiffNano;

        //timepo anterior
        double previousTimeNano;

        previousTimeNano = System.nanoTime();

        while (true) {

            //calcula la diferencia
            timeDiffNano = System.nanoTime() - previousTimeNano;

            if (timeDiffNano > 9000000) {   //tiempo puesto a boleo

                //actualiza la posicion del obj
                this.updatePosition();

                //actualiza el tiempo anterior cada vez q entra
                previousTimeNano = System.nanoTime();
            }

            //para que el procesador no pete
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
