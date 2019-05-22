package killergamePau;


public abstract class Autonomous extends Alive implements Colisionable {

    public Autonomous(KillerGame kGame, double posX, double posY, int width, int height, double velX, double velY) {
        super(kGame, posX, posY, width, height, velX, velY);
    }

}
