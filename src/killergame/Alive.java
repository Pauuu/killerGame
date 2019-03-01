package killergame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public abstract class Alive extends VisibleObject implements Runnable {

    //quitar las velocidades x defecto
    protected double velX;
    protected double velY;

    public Alive(KillerGame kGame, int posX, int posY, int width, int height) {
        super(kGame, posX, posY, width, height);

    }

    protected void updateHitBox() {
        this.hitBox.setLocation(this.posX, this.posY);
    }

    protected void testColision() {
        this.killerGame.testColision(this);

    }

    protected void updatePosition(double time) {

        double intTime = time / 10000000;    //lo pasamos a decisegundos

        //  position' = position + (vel * time); -> M.R.U.
        this.posX += this.velX * intTime;
        this.posY += this.velY * intTime;
        this.updateHitBox(); //quitar? ---> ha de llamar actualizar cada vez?
        this.testColision();
        
       

    }

    protected void invertirVelX() {
        this.velX *= -1;
    }

    protected void invertirVelY() {
        this.velY *= -1;
    }

    public void invertirVelocidades() {
        this.velX *= -1;
        this.velY *= -1;
    }

    public void moveX(double vel) {
        this.velX = vel;
        this.posX += this.velX;
//        this.setPosX(this.getPosX() + this.velX);
    }

    public void moveY(double vel) {
        this.velY = vel;
        this.posY += this.velY;

//        this.setPosY(this.getPosY() + this.velY);
    }

//    @Override
//    public void run() {
//
//        //direfencia de tiempo actual menos anterior
//        double timeDiffNano;
//
//        //timepo anterior
//        double previousTimeNano;
//
//        previousTimeNano = System.nanoTime();
//
//        while (true) {
//
//            //calcula la diferencia
//            timeDiffNano = System.nanoTime() - previousTimeNano;
//
//            if (timeDiffNano >= 10000000) {
//
//                //actualiza la posicion del obj
//                this.updatePosition(timeDiffNano);
//
//                //actualiza el tiempo anterior cada vez q entra
//                previousTimeNano = System.nanoTime();
//
////                System.out.println("timediff: " + timeDiffNano);
//            }
//
//            
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    @Override
    public void run() {

       
        while (true) {


            //actualiza la posicion del obj
            this.updatePosition(10000000);


//                System.out.println("timediff: " + timeDiffNano);
            try {
                Thread.sleep(17);
            } catch (InterruptedException ex) {
                Logger.getLogger(Autonomous.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public double getVelY() {
        return velY;
    }

}
