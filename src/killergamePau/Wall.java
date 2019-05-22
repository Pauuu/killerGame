/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergamePau;

import java.awt.Color;
import java.awt.Graphics2D;
import java.net.Socket;

/**
 * Para futura implementacion. Relacion de uso o que recoja el socket y 
 * mire su estado
 * 
 * @author pau
 */
public class Wall extends StaticObj {

    
    
    public Wall(KillerGame kGame, int posX, int height) {
        super(kGame, posX, 0, 10, height);

    }
    
    @Override
    public void run(){
        Socket sock;
        
//        if (sock != null){
//            this
//        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.red);
        g2d.fillRect((int) this.posX, (int) this.posY, this.width, this.height);
    }

}
