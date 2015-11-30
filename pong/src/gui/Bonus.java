package src.gui;

import src.util.RandomNumber;



/**
 * Created by Se Easy on 23/11/2015.
 */
public class Bonus extends PongItem {
    private static int BIGRACKET = 1;
    private static int SMALLRACKET = 2;
    private static int ROCK = 3;
    private long time;
    private long delay;
    private String image = "image/bonus.png";
    private boolean visible;
    private boolean active;
    private int playerMax = 0;
    private int SPEED_BONUS = 3;
    public long getTime(){return this.time;}
    public void setTime(){this.time = System.currentTimeMillis();}
    public boolean isActive() {return active;}
    public boolean isVisible(){return visible;}
    public void setActive(Boolean b){ active = b;}
    public void setVisible(Boolean b){visible = b;}
    public long getDelay(){
        return delay;
    }
    public Bonus getBonus(){return bonus;}
    public int getPlayerMax(){
        return playerMax;
    }
    public void setPlayerMax(int newMaxPlayer){playerMax = newMaxPlayer; }

    private Bonus bonus;

    public Bonus(){
        super(Pong.getSizePongX()/2,Pong.getSizePongY()/2);
        initImage(image);
        active = false;
        visible = false;
    }

    public void bonusAleatoire(){
        setPosition(Pong.getSizePongX()/2,Pong.getSizePongY()/2);
        int a = RandomNumber.randomValue(1,4);
        switch(a){
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
        a = RandomNumber.randomValue(1,2);
        setNumber(a);
        setVisible(true);

    }

    @Override
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

    public void setDelay(int x){
        delay = x * 1000;
    }

    /**
     * Si le delai du bonus de changement de taille de raquette est passé on arrête le bonus.
     */
    public void duration(){
        if(isActive())
            if(getNumber() == BIGRACKET || getNumber() == SMALLRACKET)
                if(time + delay < System.currentTimeMillis())
                    stopBonus();
    }


    /**
     * Fais "disparaître" le cadeau de l'écran lors d'un collision avec une raquette
     */
    public void disappear(){
        visible = false;
    }

    /**
     * Active le bonus suivant son numéro et l'item (pour le changement de taille de la raquette).
     * @param pi le PongItem qui peut être influencée par les bonus.
     */
    public void startBonus(PongItem pi){
        setTime();
        setDelay(5);
        active = true;
        switch (getNumber()){
            case 1:
                bonus = new ChangeRacketSize(pi, 1);
                break;
            case 2:
                bonus = new ChangeRacketSize(pi, -1);
                break;
        }
    }

    /**
     * Désactive le Bonus et se remet à l'état initiale les changement effectués par le Bonus.
     */
    public void stopBonus(){
        if (active && !isVisible()) {
            active = false;
            visible=false;
            switch (getNumber()) {
                case 1:
                    ChangeRacketSize c = (ChangeRacketSize) bonus;
                    c.stopChangeRacketSize();
                    break;
                case 2:
                    ChangeRacketSize s = (ChangeRacketSize) bonus;
                    s.stopChangeRacketSize();
                    break;
                case 3:
                    Rock r = (Rock) bonus;
                    r.stopRock();
                    break;

            }
        }
        setNumber(0);
    }


}
