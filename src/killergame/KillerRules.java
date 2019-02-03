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

    public void comprobarColision(VisibleObject vObj) {
        
        System.out.println("el objeto ha sido invocado");
        if (vObj instanceof Autonomous) {
            if (vObj.getPosX() > 400) {
                vObj.setPosX(0);
                vObj.setPosY(8);
            }
        }
    }

}
