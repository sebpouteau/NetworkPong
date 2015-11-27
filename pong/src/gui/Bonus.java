package src.gui;

import javafx.scene.shape.Circle;
import src.util.RandomNumber;

import java.awt.*;
import java.util.ArrayList;

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

    public long getTime(){return this.time;}
    public void setTime(){this.time = System.currentTimeMillis();}
    public boolean isActive() {return active;}
    public boolean isVisible(){return visible;}
    public void setActive(Boolean b){ active = b;}
    public void setVisible(Boolean b){visible = b;
        System.out.println("bonus "+visible);}
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
        super(40,40);
        initImage(image);
        active = false;
        visible = false;
    }

    public void bonusAléatoire(){
        setPosition(500,500);
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
    public Bonus(int nBonus, int x, int y, int sX, int sY){
        super(x,y);
        this.setSpeed(sX,sY);
        initImage(image);
        active = false;
        visible = false;
        setNumber(nBonus);
    }
    /**
     * Déplace le cadeaux représentant le bonus et le fais rebondir sur les côtés de l'écran
     * @param sizePongX longueur de la fenêtre
     * @param sizePongY largeur de la fenêtre
     */
    public void animate(int sizePongX, int sizePongY){
        if(this.visible) {
            System.out.println("je bouge");
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

    public void setDelay(int x){
        delay = x * 10;
    }

    /**
     * Si le delai du bonus de changement de taille de raquette est passé on arrête le bonus.
     */
    public void duration(){
        if(getNumber() == BIGRACKET || getNumber() == SMALLRACKET)
            if(time + delay < System.currentTimeMillis())
                stopBonus();
    }

    /**
     * Initialise et fais apparaître un cadeau bonus.
     * @param nBonus le nombre qui définit le type de Bonus.
     * @param x la position x où apparaîtra le Bonus.
     * @param y la position y où apparaîtra le Bonus.
     * @param sX la vitesse en x qu'aura le Bonus.
     * @param sY la vitesse en yqu'aura le Bonus.
     */
//    public void appearance(int nBonus, int x, int y, int sX, int sY){
//        this.setPosition(x, y);
//        this.setSpeed(sX, sY);
//        setNumber(nBonus);
//        visible = true;
//    }

    /**
     * Fais "disparaître" le cadeau de l'écran lors d'un collision avec une raquette
     */
    public void disappear(){
        visible = false;
    }

    /**
     * Renvoi le numéro du joueur du côté ou il va disparaître s'il atteint les bords de l'écran sans toucher de raquettes.
     * @return numéro du joueur qui a raté le cadeau
     */
    public int willDisappear(){
        if (this.getPositionX() < 0) {
            return 1;
        }
        else if (this.getPositionY()< 0) {
            return 3;
        }
        else if (this.getPositionX() > (Pong.getSizePongX()- this.getWidth())) {
          return 2;
        }
        else if (this.getPositionY() > (Pong.getSizePongY() - this.getHeight())) {
            return 4;
        }
        return 0;
    }

    /**
     * Active le bonus suivant son numéro et l'item (pour le changement de taille de la raquette).
     * @param pi le PongItem qui peut être influencée par les bonus.
     */
    public void startBonus(PongItem pi){
        setTime();
        setDelay(100);
        active = true;
        switch (getNumber()){
            case 1:
                bonus = new ChangeRacketSize(pi, 1);
                break;
            case 2:
                bonus = new ChangeRacketSize(pi, -1);
                break;

            case 3:
                bonus = new Rock();
                break;
        }
    }

    /**
     * Désactive le Bonus et se remet à l'état initiale les changement effectués par le Bonus.
     */
    public void stopBonus(){
        if (active && !isVisible()) {
            active = false;

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
