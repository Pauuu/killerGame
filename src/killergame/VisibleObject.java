package killergame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class VisibleObject implements Renderizable {
    
    //permite ser accedido a solo las clases que hereden de esta (en teoria)

    protected BufferedImage imagen;

    protected KillerGame killerGame;

    protected int height;
    protected int posX;
    protected int posY;
    protected int witdh;

    public VisibleObject(KillerGame kGame, int width, int height) {
        this.killerGame = kGame;
        this.witdh = width;
        this.height = height;
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
    public void render(Graphics2D g2d) {
      //  g2d.drawImage(this.imagen, this.posX, this.posY, null);
      g2d.setColor(Color.red);
      g2d.fillOval(this.posX, this.posY, this.witdh, this.height);
    }

}
