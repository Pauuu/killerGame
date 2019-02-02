package killergame;

import java.awt.image.BufferedImage;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public class Alive extends VisibleObject implements Runnable {

    private int velX;
    private int velY;

    public Alive(KillerGame kGame) {
        super(kGame);
        
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.setPosX(this.getPosX() + velX);
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    @Override
    public void run() {
        System.out.println("hola");
    }

}
