package killergame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class VisibleObject implements Renderizable {

    private BufferedImage imagen;

    private KillerGame killerGame;

    private int height;
    private int posX;
    private int posY;
    private int witdh;

    public VisibleObject(KillerGame kGame) {
        this.killerGame = kGame;
    }

    //getters & setters
    public BufferedImage getImagen() {
        return imagen;
    }

    public void setImagen(BufferedImage imagen) {
        this.imagen = imagen;
    }
    
    public void setImagen(String url) {
       try {
            //cambiar la imagen
            this.imagen = ImageIO.read(new File(url));
        } catch (IOException ex) {
            System.err.println("-- Imagen no cargada --");
            Logger.getLogger(VisibleObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public KillerGame getKillerGame() {
        return killerGame;
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

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWith() {
        return witdh;
    }

    public void setWith(int witdh) {
        this.witdh = witdh;
    }

    @Override
    public void pintar(Graphics g) {
        g.drawImage(this.imagen, this.posX, this.posY, null);
    }

}
