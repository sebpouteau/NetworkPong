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
    private int numberBonus;
    private boolean visible;
    private boolean active;
    private Bonus bonus;



    public Bonus(){}

    public Bonus(int nBonus, int x, int y, int sX, int sY){
        super(x,y);
        this.setSpeed(sX,sY);
        initImage(image);
        active = false;
        visible = false;
        numberBonus = nBonus;
    }

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

    public long getDelay(){
        return delay;
    }
    public void setDelay(int x){
        delay = x * 100;
    }
    public long getTime(){return this.time;}
    public void setTime(){this.time = System.currentTimeMillis();}
    public boolean isActive() {return active;}
    public boolean isVisible(){return visible;}
    public void setActive(Boolean b){ active = b;}
    public void setVisible(Boolean b){visible = b;}
    public Bonus getBonus(){return bonus;}


    public void duration(){
        if(numberBonus == BIGRACKET || numberBonus == SMALLRACKET)
            if(time + delay < System.currentTimeMillis())
                stopBonus();
    }

    public void appearance(int nBonus, int x, int y, int sX, int sY){
        this.setPosition(x, y);
        this.setSpeed(sX, sY);
        numberBonus = nBonus;
        visible = true;
    }

    public void disappear(){
        visible = false;
    }

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

    public void startBonus(PongItem pi){
        setTime();
        setDelay(10);
        active = true;
        switch (numberBonus){
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

    public void stopBonus(){
        active = false;
        switch (numberBonus){
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
        numberBonus = 0;
    }


}
