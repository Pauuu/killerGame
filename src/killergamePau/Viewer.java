package killergamePau;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Viewer extends Canvas implements Runnable {

    //no tenerlo como atributo de clase (?)
    private ArrayList<VisibleObject> visibleObjects;

    private BufferedImage biFrame;
    private BufferedImage backgroundImg;

    private Graphics2D g2d;

    private KillerGame killerGame;

    //si la vetnana cambia tamaño, las variables se cambian tmb?
    private int heigth;
    private int width;

    public Viewer(KillerGame kGame, int width, int heigth) {
        this.killerGame = kGame;

        this.heigth = heigth;
        this.width = width;
        this.biFrame = new BufferedImage(
                this.width,
                this.heigth,
                BufferedImage.TYPE_4BYTE_ABGR);

        this.g2d = (Graphics2D) biFrame.getGraphics();

        //cargar imagen de "fondo"
        try {
            //--cambiar la imagen--
            this.backgroundImg = ImageIO.read(new File("imgs/seaBackground.png"));
        } catch (IOException ex) {
            System.err.println("-- Imagen no cargada --");
            Logger.getLogger(VisibleObject.class.getName()).log(Level.SEVERE, null, ex);

        }

        this.modifyAplphaChannel(this.backgroundImg, 100);

        this.setBackground(Color.blue);
        this.setSize(this.width, this.heigth); //cambiar en un futuro

    }

    private void drawInfo() {

        // ip puerto del servidor
        this.g2d.setFont(new Font("Arial", Font.BOLD, 11));

        // ip puerto del killerLeft
        if (this.killerGame.getKillerLeft().getSocket() != null) {

            if (this.killerGame.getKillerLeft().getSocket().isConnected()) {
                this.g2d.setColor(Color.GREEN);
            }

        } else {
            this.g2d.setColor(Color.RED);
        }

        this.g2d.drawString(
                "VHL Client Ip: " + this.killerGame.getKillerLeft().getClientIp(),
                5, 20);

        //////////////////////////////////////
        if (this.killerGame.getKillerLeft().getSocket() != null) {
            this.g2d.setColor(Color.GREEN);

        } else {
            this.g2d.setColor(Color.RED);
        }

        this.g2d.drawString("VHL Client Port: " + this.killerGame.getKillerLeft().getPort(),
                5, 40);

        ////////////////////////
        // ip puerto de el mismo
        this.g2d.setColor(Color.WHITE);
        this.g2d.drawString(
                "Server Ip: " + this.killerGame.getKillerServer().getIp(),
                250, 20);

        this.g2d.drawString("Server Port: " + this.killerGame.getKillerServer().getServerPort(),
                250, 40);
        /////////////////////////

        // ip puerto del killerRight
        if (this.killerGame.getKillerRight().getSocket() != null) {
            this.g2d.setColor(Color.GREEN);

        } else {
            this.g2d.setColor(Color.RED);
        }
        this.g2d.drawString(
                "VHR Client Ip: " + this.killerGame.getKillerRight().getClientIp(),
                500, 20);

        ///////////////////////////
        if (this.killerGame.getKillerRight().getSocket() != null) {
            this.g2d.setColor(Color.GREEN);

        } else {
            this.g2d.setColor(Color.RED);
        }
        this.g2d.drawString("VHR Client Port: " + this.killerGame.getKillerRight().getPort(),
                500, 40);

    }

    private void paintComponents() {

        try {
            this.g2d.fillRect(0, 0, 1920, 1080);

            for (int index = 0; index < this.visibleObjects.size(); index++) {
                this.visibleObjects.get(index).render(this.g2d);
            }

//            this.g2d.drawImage(this.backgroundImg, 0, 0, null);

            this.drawInfo();
            this.g2d.setColor(Color.blue);

        } catch (Exception e) {
            System.err.println(e);
        }
    }


    private void modifyAplphaChannel(BufferedImage bImg, int lvl) {
        byte[] b = ((DataBufferByte) bImg.getRaster().getDataBuffer()).getData();
        int[] bInt = new int[b.length];

        //casteo a int de la raster
        for (int pos = 0; pos < b.length; pos++) {
            bInt[pos] = Byte.toUnsignedInt(b[pos]);
        }

        //modificar el alfa de la imagen
        for (int pos = 2; pos < bInt.length; pos += 4) {
            bInt[pos] = 200;
        }

        //actualizar array (puntero) con los nuevos valores y casteado
        for (int pos = 0; pos < bInt.length; pos++) {
            b[pos] = (byte) bInt[pos];
        }
    }

    @Override
    public void run() {

        //this.createBufferStrategy(2);
        while (true) {

            //actualiza lista de objetos visibles
            this.visibleObjects = this.killerGame.getVisibleObjects();

            //pinta todos los elementos
            this.paintComponents();
            this.getGraphics().drawImage(biFrame, 0, 0, null);

            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
