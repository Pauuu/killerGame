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
public class Bala extends Autonomous {

    private Autonomous origenBala;
    private int distancia = 0;

    public Bala(KillerGame kGame, Autonomous aObj) {
        super(kGame, aObj.posX, aObj.posX, 10, 10);
       // --cambiar--
                
        this.origenBala = aObj;
        this.posX = aObj.posX;
        this.posY = aObj.posY;

        this.velX = 1;
        this.velY = 0;
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);
    }

    @Override
    public void run() {
        //direfencia de tiempo actual menos anterior
        double timeDiffNano;

        //timepo anterior
        double previousTimeNano;

        //posicion anterior antes de moverse;
        int prePosX;

        previousTimeNano = System.nanoTime();

        while (this.distancia < 100) {

            //calcula la diferencia
            timeDiffNano = System.nanoTime() - previousTimeNano;

            //tiempo puesto a boleo pero que va bien
            if (timeDiffNano >= 10000000) {
                System.out.println("test " + this.distancia);

                //actualiza la posicion del obj
                
                prePosX = this.posX;
                this.updatePosition(timeDiffNano);
                
                this.distancia += this.posX - prePosX;

                //actualiza el tiempo anterior cada vez q entra
                previousTimeNano = System.nanoTime();

            }

            //para que el procesador no pete?
            try {
                Thread.sleep(9);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.deleteThisFromVisibleObjs();
    }

}
