package killergame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public class Alive extends VisibleObject implements Runnable {

    //quitar las velocidades x defecto
    protected int velX;
    protected int velY;


    public Alive(KillerGame kGame, int width, int height) {
        super(kGame, width, height);

    }

    //no dejar de lado OJO
    public boolean testColision(ArrayList<VisibleObject> vObjs) {
        

        return false;
    }

    public void moveX() {
        this.setPosX(this.getPosX() + this.velX);
    }

    public void moveY() {
        this.setPosY(this.getPosY() + this.velY);
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getVelY() {
        return velY;
    }

    @Override
    public void run() {
        System.out.println("hola");
    }

}
