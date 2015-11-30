package src.gui;

import java.awt.*;

/**
 * Created by Se Easy on 26/11/2015.
 */
public class ChangeRacketSpeed extends Bonus{
  private int oldSpeed;
    private int changeSpeed = 2;
    private PongItem oldPongItem;

    public ChangeRacketSpeed(PongItem pi, int i){

        startChangeRacketSpeed(pi, i);
    }

    /**
     *Change la taille de la raquette en gardant ses anciennes dimensions
     * @param pi la raquette que l'on agrandit ou retrécie.
     * @param i vaut +1 ou -1 suivant si l'on souhaite agrandir la raquette ou la retrécir.
     */
    public void startChangeRacketSpeed(PongItem pi, int i){
        if(pi  instanceof Racket) {
            Racket r =(Racket) pi;
            oldPongItem = r;
            oldSpeed = r.getSpeedRacket();
            r.setSpeedRacket(r.getSpeedRacket() + i*changeSpeed);

        }
    }

    /**
     * Rend la taille d'origine de la raquette
     */
    public void stopChangeRacketSpeed(){
        if(oldPongItem instanceof Racket){
            Racket r = (Racket) oldPongItem;
            r.setSpeedRacket(oldSpeed);
        }
    }

}
