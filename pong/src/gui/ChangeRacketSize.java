package src.gui;

import java.awt.*;

public class ChangeRacketSize extends Bonus {
    private int sizeChange = 20;
    private PongItem oldPongItem;
    private Rectangle oldPIRect;


    public ChangeRacketSize(PongItem item, int i){
        startChangeRacketSize(item, i);
    }

    /**
     *Change la taille de la raquette en gardant ses anciennes dimensions
     * @param item la raquette que l'on agrandit ou retrécie.
     * @param i vaut +1 ou -1 suivant si l'on souhaite agrandir la raquette ou la retrécir.
     */
    public void startChangeRacketSize(PongItem item, int i){
        oldPongItem = item;
        oldPIRect = new Rectangle(item.getWidth(), item.getHeight());
        if(item.getNumber() < 3){
            item.setHeight(item.getHeight() + i*sizeChange);
        }
        else{
            item.setWidth(item.getWidth() + i * sizeChange);
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
