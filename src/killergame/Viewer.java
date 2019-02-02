package killergame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Viewer extends Canvas implements Runnable {

    //no tenerlo como atributo de clase
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

        this.setBackground(Color.blue);
        this.setSize(this.width, this.heigth); //cambiar en un futuro

    }

    private void paintComponents(Graphics2D g) {
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
        BufferedImage biFrame;
        BufferedImage bBack;

        biFrame = new BufferedImage(
                this.width,
                this.heigth,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = (Graphics2D) biFrame.getGraphics();

//        bBack = new BufferedImage(
//                this.width,
//                this.heigth,
//                BufferedImage.TYPE_4BYTE_ABGR);
        g2d.setColor(Color.red);
        g2d.fillRect(0, 0, this.width, this.heigth);
        
        this.paintComponents(g2d);

//        bFrame.getGraphics().drawImage(bFrame, this.width, this.heigth, null);
//        pinta la imagen bFrame en el canvas
        this.getGraphics().drawImage(biFrame, 0, 0, null);
//        this.visibleObjects.get(0).pintar(this.getGraphics());
    }

    @Override
    public void run() {
        //como es que se actualiza?
        this.visibleObjects = this.killerGame.getAoutonomousObjects();

        this.updateFrame();

    }

}
