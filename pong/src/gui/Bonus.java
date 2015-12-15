package src.gui;

import src.util.RandomNumber;

public class Bonus extends PongItem {

    private final static int BIGRACKET = 1;
    private final static int SMALLRACKET = 2;
    private final static int QUICKRACKET = 3;
    private final static int SLOWRACKET = 4;

    private long time;
    private long delay;
    private String image = "image/bonus.png";
    private boolean visible;
    private boolean active;
    private int playerMax = 0;
    private int SPEED_BONUS = 3;
    private Bonus bonus;

    public Bonus(){
        super(Pong.getSizePongX()/2,Pong.getSizePongY()/2);
        initImage(image);
        active = false;
        visible = false;
    }

    /* =================================================
                      Getter and Setter
       ================================================= */

    public long getTime(){return this.time;}

    public void setTime(){this.time = System.currentTimeMillis();}

    public boolean isActive() {return active;}

    public void setActive(Boolean b){ active = b;}

    public boolean isVisible(){return visible;}

    public void setVisible(Boolean b){visible = b;}

    public long getDelay(){
        return delay;
    }

    public void setDelay(int x){
        delay = x * 1000;
    }

    public Bonus getBonus(){return bonus;}

    public int getPlayerMax(){
        return playerMax;
    }

    public void setPlayerMax(int newMaxPlayer){playerMax = newMaxPlayer; }

    /* =================================================
                      Functions
       ================================================= */

    /**
     *
     */
    public void bonusAleatoire(){
        setPosition(Pong.getSizePongX()/2,Pong.getSizePongY()/2);
        int speed = RandomNumber.randomValue(1,4);
        switch(speed){
            case 1:
                this.setSpeed(3 , 3);
                break;
            case 2:
                this.setSpeed(3, -3);
                break;
            case 3:
                this.setSpeed(-3, 3);
                break;
            case 4:
                this.setSpeed(-3, -3);
                break;
        }
        int numBonus = RandomNumber.randomValue(1,4);
        setNumber(numBonus);
        setVisible(true);
    }

    /**
     * Deplace le cadeau representant le bonus si il est visible et le fait rebondir sur les cotes de l'ecran.
     * @param sizePongX Longueur de la fenetre.
     * @param sizePongY Largeur de la fenetre.
     */
    public void animate(int sizePongX, int sizePongY){
        if(this.visible) {
            this.setPosition((this.getPositionX() + this.getSpeedX()),(this.getPositionY() + this.getSpeedY()));
            if (this.getPositionX() < 0) {
                this.setPositionX(0);
                this.setSpeedX(-this.getSpeedX());
            }
            else if (this.getPositionY()< 0) {
                this.setPositionY(0);
                this.setSpeedY(-this.getSpeedY());
            }
            else if (this.getPositionX() > (sizePongX - this.getWidth())) {
                this.setPositionX(sizePongX - this.getWidth());
                this.setSpeedX(-this.getSpeedX());
            }
            else if (this.getPositionY() > (sizePongY - this.getHeight())) {
                this.setPositionY(sizePongY - this.getHeight());
                this.setSpeedY(-this.getSpeedY());
            }
            this.setPositionRectangle(this.getPositionX(), this.getPositionY());
        }
        duration();
    }

   @Override
    public boolean notCheating(int x, int y, int speedX,int speedY){
        return Math.abs(this.getPositionX() - x) <= SPEED_BONUS*2&&
                Math.abs(this.getPositionY() - y) <= SPEED_BONUS*2&&
                Math.abs(this.getSpeedX() - speedX) <= SPEED_BONUS*2&&
                Math.abs(this.getSpeedY() - speedY) <=SPEED_BONUS*2;
    }

    /**
     * Si le delai du bonus est passe on arrete le bonus.
     */
    public void duration(){
        if(isActive())
            if(time + delay < System.currentTimeMillis())
                stopBonus();
    }

    /**
     * Fais "disparaitre" le cadeau de l'ecran lors d'un collision avec une raquette.
     */
    public void disappear(){
        visible = false;
    }

    /**
     * Active le bonus suivant son numéro et l'item (pour le changement de taille de la raquette).
     * @param PongItem le PongItem qui peut être influencée par les bonus.
     */
    public void startBonus(PongItem PongItem){
        setTime();
        setDelay(5);
        active = true;
        switch (getNumber()){
            case BIGRACKET:
                bonus = new ChangeRacketSize(PongItem, 1);
                break;
            case SMALLRACKET:
                bonus = new ChangeRacketSize(PongItem, -1);
                break;
            case QUICKRACKET:
                bonus = new ChangeRacketSpeed(PongItem, 1);
                break;
            case SLOWRACKET:
                bonus = new ChangeRacketSpeed(PongItem, -1);
        }
    }

    /**
     * Desactive le Bonus et se remet à l'etat initiale les changements effectues par le Bonus.
     */
    public void stopBonus(){
        if (active && !isVisible()) {
            active = false;
            visible=false;
            switch (getNumber()) {
                case BIGRACKET:
                    ChangeRacketSize c = (ChangeRacketSize) bonus;
                    c.stopChangeRacketSize();
                    break;
                case SMALLRACKET:
                    ChangeRacketSize s = (ChangeRacketSize) bonus;
                    s.stopChangeRacketSize();
                    break;
                case QUICKRACKET:
                    ChangeRacketSpeed crs = (ChangeRacketSpeed) bonus;
                    crs.stopChangeRacketSpeed();
                    break;
                case SLOWRACKET:
                    ChangeRacketSpeed cs = (ChangeRacketSpeed) bonus;
                    cs.stopChangeRacketSpeed();
                    break;
            }
        }
        setNumber(0);
    }

}
