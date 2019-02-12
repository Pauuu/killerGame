package killergame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Autonomous extends Alive implements Colisionable {

    public Autonomous(KillerGame kGame, int width, int height) {
        super(kGame, width, height);
    }

}
