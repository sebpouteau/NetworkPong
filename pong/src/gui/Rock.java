package src.gui;

import java.awt.*;

/**
 * Created by ysaemery on 25/11/15.
 */
public class Rock extends Bonus{
    private int lifePointRock;
    private Rectangle rock = new Rectangle(3,3);

    public Rock(){
        startRock();
    }

    public Rectangle getRock(){return rock;}
    public int getLifePointRock(){return lifePointRock;}
    public void setLifePointRock(int p){lifePointRock = p;}

    /**
     * Remet les points de vie du rocher à 3
     */
    public void startRock(){
        lifePointRock = 3;
    }

    /**
     * Rien pour le moment
     */
    public void stopRock(){}

}
