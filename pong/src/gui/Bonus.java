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
    private static int DUOBALL = 4;
    private long time;
    private long delay;
    private String image = "image/bonus.png";
    private int numberBonus;
    private boolean visible;
    private boolean active;
    private PongItem oldPongItem;
    private Rectangle oldPIRect;
    private Rectangle bigRect = new Rectangle(15,100);
    private Rectangle bigRectH = new Rectangle(100, 15);
    private Rectangle smallRect = new Rectangle(15,60);
    private Rectangle smallRectH = new Rectangle(60,15);
    private int lifePointRock;
    private Rectangle rock = new Rectangle(3,3);

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
    public Rectangle getRock(){return rock;}
    public int getLifePointRock(){return lifePointRock;}
    public void setLifePointRock(int p){lifePointRock = p;}

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
        if (nBonus == 3){
            rock.setLocation(x, y);
        }
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
        active = true;
        switch (numberBonus){
            case 1:
                startBigRacket(pi);
                break;
            case 2:
                startSmallRacket(pi);
                break;
            case 3:
                startRock();
                break;
        }
    }
    public void stopBonus(){
        active = false;
        switch (numberBonus){
            case 1:
                stopRacketChange();
                break;
            case 2:
                stopRacketChange();
                break;
            case 3:
                stopRock();
                break;

        }
        numberBonus = 0;
    }

    public void startBigRacket(PongItem pi){
        time = System.currentTimeMillis();
        setDelay(10);
        oldPongItem = pi;
        oldPIRect = new Rectangle(pi.getWidth(), pi.getHeight());
        if(pi.getNumber() < 3){
            pi.setWidth((int) bigRect.getWidth());
            pi.setHeight((int) bigRect.getHeight());
        }
        else{
            pi.setWidth((int) bigRectH.getWidth());
            pi.setHeight((int) bigRectH.getHeight());
        }
    }

    public void startSmallRacket(PongItem pi){
        time = System.currentTimeMillis();
        setDelay(10);
        oldPongItem = pi;
        oldPIRect = new Rectangle(pi.getWidth(), pi.getHeight());
        if(pi.getNumber() < 3){
            pi.setWidth((int) smallRect.getWidth());
            pi.setHeight((int) smallRect.getHeight());
        }
        else{
            pi.setWidth((int) smallRectH.getWidth());
            pi.setHeight((int) smallRectH.getHeight());
        }
    }

    public void stopRacketChange(){
        oldPongItem.setHeight((int)oldPIRect.getHeight());
        oldPongItem.setWidth((int) oldPIRect.getWidth());
        numberBonus = 0;
    }

    public void startRock(){
        lifePointRock = 3;
    }

    public void stopRock(){

    }

}
