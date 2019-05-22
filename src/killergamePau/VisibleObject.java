package killergamePau;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Shape;
import javax.imageio.ImageIO;

/**
 * Todo objeto visible, una vez instanciado es añadido a la lista de objetos
 * visibles del objeto killerGame gracias al metodo <i>addVisibleObject</i>.
 *
 * De esta manera, cunado es creado, en la pantalla se pinta auntomaticamente.
 *
 * @author pau
 */
public abstract class VisibleObject implements Renderizable {

    //permite ser accedido a solo las clases que hereden de esta (en teoria)
    protected BufferedImage imagen = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

    protected KillerGame killerGame;

    protected Rectangle hitBox;

    protected boolean alive;

    protected int height;
    protected double posX;
    protected double posY;
    protected int width;

    //otro construcoe para indicar la posicion?
    public VisibleObject(KillerGame kGame, double posX, double posY, int width, int height) {
        this.killerGame = kGame;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.alive = true;

        // añadir imagen a la bola
//        this.setImagen("imgs/1.png");

        this.addHitBox();

        // añadir objeto en la lista de visible objects del killergame
        this.killerGame.addVisibleObject(this);

    }

    protected void addHitBox() {
        this.hitBox = new Rectangle(
                (int) this.posX,
                (int) this.posY,
                this.width,
                this.height
        );

    }

    protected void kill() {

        ArrayList<VisibleObject> listVisibleObjs;

        // puntero a la lista de visual objects del killerGame
        listVisibleObjs = this.killerGame.getVisibleObjects();

        for (int pos = 0; pos < listVisibleObjs.size(); pos++) {
            if (this.killerGame.getVisibleObjects().get(pos) == this) {
                this.alive = false;
                this.killerGame.getVisibleObjects().remove(pos);
            }
        }
    }

    //getters & setters
    public BufferedImage getImagen() {
        return imagen;
    }

    public KillerGame getKillerGame() {
        return killerGame;
    }

    // getters y setters
    public void setImagen(BufferedImage imagen) {
        this.imagen = imagen;
    }

    public void setImagen(String url) {
        try {
            // --cambiar la imagen--
            this.imagen = ImageIO.read(new File(url));

        } catch (IOException ex) {
            System.err.println("-- Imagen no cargada --");
            Logger.getLogger(VisibleObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setKillerGame(KillerGame killerGame) {
        this.killerGame = killerGame;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Rectangle getHitBox() {
        return this.hitBox;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public void render(Graphics2D g2d) {

    }

}
