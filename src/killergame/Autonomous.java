package killergame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Autonomous extends Alive implements Colisionable {

    public Autonomous(KillerGame kGame, int posX, int posY, int width, int height) {
        super(kGame, posX, posY, width, height);
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

            //tiempo puesto a boleo pero que va bien
            if (timeDiffNano >= 10000000) {

                //actualiza la posicion del obj
                this.updatePosition(timeDiffNano);

                //actualiza el tiempo anterior cada vez q entra
                previousTimeNano = System.nanoTime();

//                System.out.println("timediff: " + timeDiffNano);

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
