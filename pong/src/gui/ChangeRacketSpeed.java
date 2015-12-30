package src.gui;

public class ChangeRacketSpeed extends Bonus{

    private int oldSpeed;
    private int changeSpeed = 2;
    private PongItem oldPongItem;

    public ChangeRacketSpeed(PongItem item, int i){
        startChangeRacketSpeed(item, i);
    }

    /**
     *Change la taille de la raquette en gardant ses anciennes dimensions
     * @param item la raquette que l'on agrandit ou retrécie.
     * @param i vaut +1 ou -1 suivant si l'on souhaite agrandir la raquette ou la retrécir.
     */
    public void startChangeRacketSpeed(PongItem item, int i){
        if(item  instanceof Racket) {
            Racket r =(Racket) item;
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
