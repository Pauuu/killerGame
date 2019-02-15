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
public class Bala extends Autonomous{
    
    private Autonomous origenBala;
    private int recorrido;

    public Bala(KillerGame kGame, Autonomous aObj, int posX, int posY) {
        super(kGame, posX, posY, 10, 10);
        
        this.origenBala = aObj;
        this.velY = 0;
    }
    
    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.pink);
        g2d.fillOval(this.posX, this.posY, this.witdh, this.height);

    }

    @Override
    public void run() {
        //direfencia de tiempo actual menos anterior
        double timeDiffNano;

        //timepo anterior
        double previousTimeNano;

        previousTimeNano = System.nanoTime();

        while (this.recorrido < 1000) {
            

            //calcula la diferencia
            timeDiffNano = System.nanoTime() - previousTimeNano;

            //tiempo puesto a boleo pero que va bien
            if (timeDiffNano >= 10000000) {
                            System.out.println("test");


                //actualiza la posicion del obj
                this.updatePosition(timeDiffNano);
                this.recorrido += posX; 
                
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
    }
    
}
