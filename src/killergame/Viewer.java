package killergame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Viewer extends Canvas implements Runnable {

    private ArrayList<VisibleObject> visibleObjects;

    private BufferedImage newBi;    //borrar(?)

    private KillerGame killerGame;

    //si la vetnana cambia tamaño, las variables se cambian tmb?
    private int heigth;
    private int width;

    public Viewer(KillerGame kGame, int heigth, int width) {
        this.killerGame = kGame;

        this.heigth = heigth;
        this.width = width;

       // this.setBackground(Color.blue);
        this.setSize(this.width, this.heigth); //cambiar en un futuro

    }

    private void paintComponents(Graphics g) {
        try {
            for (VisibleObject vo : visibleObjects) {
                vo.pintar(g);
            }
        } catch (Exception e) {
            System.out.println("== A ver, que aqui no hay lista ningna ==");
            System.out.println("=== Solo debug ===");
        }
    }

    private void updateFrame() {
        BufferedImage bFrame;
        BufferedImage bBack;

        bFrame = new BufferedImage(
                this.width,
                this.heigth,
                BufferedImage.TYPE_3BYTE_BGR);

        bBack = new BufferedImage(
                this.width,
                this.heigth,
                BufferedImage.TYPE_3BYTE_BGR);

        this.paintComponents(bBack.getGraphics());

        bFrame.getGraphics().drawImage(bBack, this.width, this.heigth, null);

//        pinta la imagen bFrame en el canvas
        this.getGraphics().drawImage(bFrame, 0, 0, null);
    }

    @Override
    public void run() {

        this.visibleObjects = this.killerGame.getAoutonomousObjects();

        while (true) {
            this.updateFrame();
        }
    }

}
