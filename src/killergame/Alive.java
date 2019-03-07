package killergame;

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
    protected long time;

    public Alive(
            KillerGame kGame,
            double posX, double posY,
            int width, int height,
            double velX, double velY) {

        super(kGame, posX, posY, width, height);

        this.velX = velX;
        this.velY = velY;

        new Thread(this).start();

    }

    protected void updateHitBox() {
        this.hitBox.setLocation((int) this.posX, (int) this.posY);
    }

    protected void testColision() {
        this.killerGame.testColision(this);

    }

    protected void updatePosition() {

        double timeDiff = (System.nanoTime() - this.time) / 1000000d;

//        double intTime = time / 10000000d;    //lo pasamos a decisegundos
        //  position' = position + (vel * time); -> M.R.U.
        this.posX += this.velX * 1d;
        this.posY += this.velY * 1d;
//
//        System.out.println("Ball pos X: " + this.velX);
//        System.out.println("Ball pos Y: " + this.velY);
//
//        this.time = System.nanoTime();

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

    public void setVelX(double vel) {
        this.velX = vel;
        this.posX += this.velX;
//        this.setPosX(this.getPosX() + this.velX);
    }

    public void setVelY(double vel) {
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

        while (alive) {
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
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

//    public void setVelX(int velX) {
//        this.velX = velX;
//    }
//
//    public void setVelY(int velY) {
//        this.velY = velY;
//    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
