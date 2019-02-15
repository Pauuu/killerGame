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
    private boolean dead = false;

    public Ball(KillerGame kGame, int posX, int posY, int width, int height) {
        super(kGame, posX, posY, width, height);
        this.velX = 5;  //quitar?
        this.velY = 5;  //quitar?
      

        //this.setImagen("imgs/blackBall.png");
    }
    
    public void disparo(){
        new Thread(new Bullet(this.killerGame, this)).start();
    }

    public void setColor(Color c) {
        this.c = c;
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(c);
        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);

    }
//
//    @Override
//    public void run() {
////
////        //direfencia de tiempo actual menos anterior
////        double timeDiffNano;
////
////        //timepo anterior
////        double previousTimeNano;
////
////        previousTimeNano = System.nanoTime();
////
////        while (true) {
////
////            //calcula la diferencia
////            timeDiffNano = System.nanoTime() - previousTimeNano;
////
////            //tiempo puesto a boleo pero que va bien
////            if (timeDiffNano >= 10000000) {
////
////                //actualiza la posicion del obj
////                this.updatePosition(timeDiffNano);
////
////                //actualiza el tiempo anterior cada vez q entra
////                previousTimeNano = System.nanoTime();
////
////                System.out.println("timediff: " + timeDiffNano);
////
////            }
////
////            //para que el procesador no pete?
////            try {
////                Thread.sleep(9);
////            } catch (InterruptedException ex) {
////                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
////            }
////        }
//    }

}
