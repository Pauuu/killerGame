package killergame;

public class Autonomous extends Alive {

    public Autonomous(KillerGame kGame) {
        super(kGame);
//        this.setWith(20);
//        this.setHeight(20);
        this.setImagen("imgs/cocinero.png");
    }

    public void moveX(int x) {
        this.setPosX(this.getPosX() + x);
    }

    @Override
    public void run() {
      

//        this.setVelX(10);
            this.moveX(1);
        
    }

}
