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
        
//        System.out.println("el objeto ha sido invocado");
        if (vObj instanceof Autonomous) {
            //alguna manera de no tener que crar una nueva variable?
            Autonomous aObj = (Autonomous) vObj;
            
            //mira si colision en eje Y
            if (vObj.getPosY() >= 400 - aObj.getHeight() || vObj.getPosY() <= 0) {
                aObj.setVelY(aObj.getVelY() * -1);
              
            }
            
        }
    }

}
