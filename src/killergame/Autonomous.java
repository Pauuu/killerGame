package killergame;


public abstract class Autonomous extends Alive implements Colisionable {

    public Autonomous(KillerGame kGame, int posX, int posY, int width, int height) {
        super(kGame, posX, posY, width, height);
    }

}
