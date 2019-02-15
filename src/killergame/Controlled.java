package killergame;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public abstract class Controlled extends Alive {

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated
     */
    public Controlled(KillerGame kGame, int posX, int posY, int width, int height) {
        super(kGame, posX, posY, width, height);
    }
    
    

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
