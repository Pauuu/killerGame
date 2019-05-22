/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergamePau;

/**
 *
 * @author pau
 */
public class KillerRules {

    public KillerRules() {

    }

    public static int testColision(VisibleObject vObj1, VisibleObject vObj2) {
        
        
        
        if ((vObj1 instanceof Ball) &&
                (vObj2 instanceof Ball)) {
            return 1;
        }
        
        if ((vObj1 instanceof Alive) &&
                (vObj2 instanceof Bullet)) {
            
            return 2;
        }
        
        if ((vObj1 instanceof KillerShip) && (vObj2 instanceof Ball)){
            return 3;
        }
       

        return 0;
    }
}
