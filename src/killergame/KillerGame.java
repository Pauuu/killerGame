package killergame;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;

class KillerGame extends JFrame {

    private ArrayList<KillerClient> killerClients;
    private ArrayList<Autonomous> autonomousObjects;
    private KillerServer killerServer;
    private int width;
    private int height;
    private Viewer viewer;

    public KillerGame() {
        this.setFrameSize(300, 300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(this.width, this.height);
        this.setVisible(true);

        this.killerClients = new ArrayList<>(); //x si es cliente de mas maquinas
        this.autonomousObjects = new ArrayList<>();
        this.killerServer = new KillerServer(this);
        
        this.addViewer();
        this.addBola(this.viewer);
        
        //lanza un nuevo hilo de ejecucion
        this.viewer.run();
    }

    private void addViewer() {

        this.viewer = new Viewer(this, this.height, this.width);
        this.getContentPane().add(this.viewer);
    }
    
    private void addBola(Viewer v){
        Autonomous bola = new Autonomous(this);
        this.autonomousObjects.add(bola);
        
    }

    private void setFrameSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    //Getters & setters
    public ArrayList getAoutonomousObjects(){
        return this.autonomousObjects;
    }
}
