package src.gui;

import javafx.scene.control.TextFormatter;
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
    private static int QUICKRACKET = 4;
    private static int SLOWRACKET = 5;
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
        super(0,0);
        initImage(image);
        active = false;
        visible = false;
    }

    public void bonusAleatoire(){
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
        a = RandomNumber.randomValue(1,5);
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
        delay = x * 1000;
    }

    /**
     * Si le delai du bonus de changement de taille de raquette est passé on arrête le bonus.
     */
    public void duration(){
        if(isActive())
//            if(getNumber() != ROCK)
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
        System.out.println("je rentre dans start bonus");
        active = true;
        System.out.println(getNumber());
        switch (getNumber()){
            case 1:
                bonus = new ChangeRacketSize(pi, 1);
                break;
            case 2:
                bonus = new ChangeRacketSize(pi, -1);
                break;

            case 3:
                System.out.println("je suis un rocher");
                bonus = new Rock();
                break;
            case 4:
                System.out.println("j'accèlère");
                bonus = new ChangeRacketSpeed(pi, 1);
                break;
            case 5:
                System.out.println("je ralentis");
                bonus = new ChangeRacketSpeed(pi, -1);
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
                case 4:
                    ChangeRacketSpeed crs = (ChangeRacketSpeed) bonus;
                    crs.stopChangeRacketSpeed();
                    break;
                case 5:
                    ChangeRacketSpeed cs = (ChangeRacketSpeed) bonus;
                    cs.stopChangeRacketSpeed();
                    break;

            }
        }
        setNumber(0);
    }


}
