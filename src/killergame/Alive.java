package killergame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public abstract class Alive extends VisibleObject implements Runnable {

    //quitar las velocidades x defecto
    protected int velX;
    protected int velY;

    public Alive(KillerGame kGame, int width, int height) {
        super(kGame, width, height);

    }

    protected void updateHitBox() {
        this.hitBox.setLocation(this.posX, this.posY);
    }

    protected void testColision() {
        this.killerGame.testColision(this);

    }

    protected void updatePosition() {

        this.moveX((int) (this.velX));
        this.moveY((int) (this.velY));
        this.updateHitBox(); //quitar? ---> ha de llamar actualizar cada vez?
        this.testColision();

    }

    protected void invertirVelX() {
        this.velX *= -1;
    }

    protected void invertirVelY() {
        this.velY *= -1;
    }

    public void invertirVelocidades(){
        this.velX *= -1;
        this.velY *= -1;
    }
    
    public void moveX(int vel) {
        this.velX = vel;
        this.setPosX(this.getPosX() + this.velX);
    }

    public void moveY(int vel) {
        this.velY = vel;
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

}
