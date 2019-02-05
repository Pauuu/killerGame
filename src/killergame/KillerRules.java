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

    public static void comprobarColision(VisibleObject vObj) {

//        System.out.println("el objeto ha sido invocado");
        if (vObj instanceof Autonomous) {
            //alguna manera de no tener que crar una nueva variable?
            Autonomous aObj = (Autonomous) vObj;

            //mira si colision en eje Y
            if (vObj.getPosY() >= 1020 - aObj.getHeight()) {
                aObj.setPosY(1020 - aObj.getHeight() -1);
                aObj.setVelY(aObj.getVelY() * -1);

            } else if (vObj.getPosY() <= 0) {
                aObj.setPosY(1);
                aObj.setVelY(aObj.getVelY() * -1);
            }

            if (vObj.getPosX() >= 1900 - aObj.getWith()) {
                aObj.setPosX(1900 - aObj.getHeight() - 1);
                aObj.setVelX(aObj.getVelX() * -1);

            } else if (vObj.getPosX() <= 0) {
                aObj.setPosX(1);
                aObj.setVelX(aObj.getVelX() * -1);
            }

        }
    }

}
