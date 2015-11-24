package src.gui;

import src.util.RandomNumber;

/**
 * Created by Se Easy on 23/11/2015.
 */
public class Bonus extends PongItem {
    private long time;
    private long delay;
    private String image = "image/bonus.png";
    private int numberBonus;
    private boolean isVisible;
    private boolean isActive;

    public Bonus(){
        super(Pong.getSizePongX()/2, Pong.getSizePongY()/2);
        this.setSpeed(-3,0);
        initImage(image);
        isActive = false;
        isVisible = false;
    }
    public void animate(int sizePongX, int sizePongY){
        if (this.getPositionX() <= 0) {
            disappear();
        }
        else if (this.getPositionY()<= 0) {
            disappear();
        }
        else if (this.getPositionX() >= (Pong.getSizePongX() - this.getWidth())) {
            disappear();
        }
        else if (this.getPositionY() >= (Pong.getSizePongY() - this.getHeight())) {
            disappear();
        }
        else{
            this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());
        }
    }

    public long getDelay(){
        return delay;
    }
    public void setDelay(int x){
        delay = x * 100;
    }
    public boolean getIsActive() {return isActive;}
    public boolean getIsVisible(){return isVisible;}

    public void duration(PongItem pi){
        if(!isActive)
            startBonus(pi);
        if(time + this.delay < System.currentTimeMillis() && isActive) {
            System.out.println("je fini");
            stopBonus(pi);
        }
    }

    public void appearance(int x, int y){
        this.setPosition(x, y);
       // numberBonus = RandomNumber.randomValue(1, 3);
        numberBonus = 1;
        isVisible = true;
    }

    public void disappear(){
        isVisible = false;
    }

    public void startBonus(PongItem pi){
        time = System.currentTimeMillis();
        isActive = true;
        switch (numberBonus){
            case 1:
                System.out.println("je commence mon bonus");
                BigRacket bR = new BigRacket(pi);
                bR.startBonus(pi);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
    public void stopBonus(PongItem pi){
            switch (numberBonus){
                case 1:
                    System.out.println("je termine mon bonus");
                    BigRacket bR = new BigRacket(pi);
                    bR.stopBonus(pi);
                    break;
                case 2:
                    break;
                case 3:
                    break;
        }
        isActive = false;

    }
}
