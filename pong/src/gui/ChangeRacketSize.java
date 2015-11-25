package src.gui;

import java.awt.*;

/**
 * Created by ysaemery on 25/11/15.
 */
public class ChangeRacketSize extends Bonus {
    private int sizeChange = 20;
    private PongItem oldPongItem;
    private Rectangle oldPIRect;


    public ChangeRacketSize(PongItem pi, int i){

        startChangeRacketSize(pi, i);
    }

    /**
     *Change la taille de la raquette en gardant ses anciennes dimensions
     * @param pi la raquette que l'on agrandit ou retrécie.
     * @param i vaut +1 ou -1 suivant si l'on souhaite agrandir la raquette ou la retrécir.
     */
    public void startChangeRacketSize(PongItem pi, int i){
        oldPongItem = pi;
        oldPIRect = new Rectangle(pi.getWidth(), pi.getHeight());
        if(pi.getNumber() < 3){
            pi.setHeight((int) pi.getHeight() + i*sizeChange);
        }
        else{
            pi.setWidth((int) pi.getWidth() + i * sizeChange);
        }
    }

    /**
     * Rend la taille d'origine de la raquette
     */
    public void stopChangeRacketSize(){
        oldPongItem.setHeight((int)oldPIRect.getHeight());
        oldPongItem.setWidth((int) oldPIRect.getWidth());
    }

}
