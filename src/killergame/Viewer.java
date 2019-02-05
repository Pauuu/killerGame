package killergame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
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

        //cargar imagen de "fondo"
        try {
            //cambiar la imagen
            this.backgroundImg = ImageIO.read(new File("imgs/seaBackground.png"));
        } catch (IOException ex) {
            System.err.println("-- Imagen no cargada --");
            Logger.getLogger(VisibleObject.class.getName()).log(Level.SEVERE, null, ex);

        }

        this.modifyAplphaChannel(this.backgroundImg, 100);

        this.setBackground(Color.blue);
        this.setSize(this.width, this.heigth); //cambiar en un futuro

    }

    private void paintComponents(Graphics2D g2d) {

        try {
            g2d.fillRect(0, 0, 1000, 1000);

            for (VisibleObject vObj : visibleObjects) {
//                g.drawImage(this.backgroundImg, 0, 0, null);

                vObj.pintar(g2d);

            }

        } catch (Exception e) {
            System.out.println("Exception on paintComponents");
            System.out.println("=== Solo debug ===");
        }
    }

    private void updateFrame() {

        Graphics2D g2d = (Graphics2D) biFrame.getGraphics();

        //poner fondo con canal aplpha y luego pintar elemento (ojo cambair orden pintada componentes)
        g2d.drawImage(this.backgroundImg, 0, 0, null);

        //pinta todos los elementos en la BufferedImage
        this.paintComponents(g2d);

        //pinta la imagen en el canvas
        this.getGraphics().drawImage(biFrame, 0, 0, null);
    }

    private void modifyAplphaChannel(BufferedImage bImg, int lvl) {
        byte[] b = ((DataBufferByte) bImg.getRaster().getDataBuffer()).getData();
        int[] bInt = new int[b.length];

        //casteo a int de la raster
        for (int pos = 0; pos < b.length; pos++) {
            bInt[pos] = Byte.toUnsignedInt(b[pos]);
        }

        //modificar el alfa de la imagen
        for (int pos = 0; pos < bInt.length; pos += 4) {
            bInt[pos] = lvl;
        }

        //actualizar array (puntero) con los nuevos valores y casteado
        for (int pos = 0; pos < bInt.length; pos++) {
            b[pos] = (byte) bInt[pos];
        }
    }

    @Override
    public void run() {
        //modificar esto 

        long timeDiffMili;      //direfencia de tiempo actual menos anterior
        long previousTimeMili;  //timepo anterior
        int fps;                //sera dividido entre 1seg para controlar freqüenica

        previousTimeMili = System.currentTimeMillis();
        fps = 64;

        while (true) {

            //calcula tiempo actual menos el anterior (diferencia tiempo)
            timeDiffMili = System.currentTimeMillis() - previousTimeMili;

            //si diferencia mayor o igual...
            if (timeDiffMili >= 1000 / fps) {

                //actualiza el tiempo anterior
                previousTimeMili = System.currentTimeMillis();

                //actualiza lista de objetos visibles
                this.visibleObjects = this.killerGame.getAoutonomousObjects();

                //pinta todos los elementos
                this.updateFrame();

            }

        }

    }

}
