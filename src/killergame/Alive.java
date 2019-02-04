package killergame;

import java.awt.image.BufferedImage;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public class Alive extends VisibleObject implements Runnable {

    //quitar las velocidades x defecto
    private int velX = 2;
    private int velY = 2;

    public Alive(KillerGame kGame, int width, int height) {
        super(kGame, width, height);

    }

    public void moveX(int x) {
        this.setPosX(this.getPosX() + this.velX);
    }
    public void moveY(int y) {
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
