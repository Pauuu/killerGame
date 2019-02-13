/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

/**
 *
 * @author pau
 */
public class KillerRules {

    public KillerRules() {

    }

    public static int testColision(VisibleObject vObj1, VisibleObject vObj2) {
        if (vObj1 instanceof Ball || vObj2 instanceof Ball) {
            return 1;
        }
        
        return 0;
    }
}
