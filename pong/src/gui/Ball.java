package src.gui;

import src.util.RandomNumber;
import java.awt.*;


public class Ball extends PongItem {

    public static final int BALL_SPEED = 4;
    private String image = "image/ball.png";


    public Ball(int id) {
        super(Pong.getSizePongX()/2,Pong.getSizePongY()/2);
        this.setNumber(id);
        initImage(image);
        this.setSpeed(new Point(BALL_SPEED, BALL_SPEED));
    }

    private void animateX(int setPos, int setSpeed){
        this.setPositionX(setPos);
        this.setSpeedX(setSpeed);
    }

    private void animateY(int setPos, int setSpeed){
        this.setPositionY(setPos);
        this.setSpeedY(setSpeed);
    }

    @Override
    public void animate(int sizePongX,int sizePongY){
        System.out.println("je dois déplacer ma balle?? " );
        if(Pong.getIfStart()) {
            this.setSpeed(0, 0);
        }
        else{
		/* Update ball position */
            if (this.getPositionX() < 0) {
                animateX(0, -this.getSpeedX());
            }
            if (this.getPositionY() < 0) {
                animateY(0, -this.getSpeedY());
            }
            if (this.getPositionX() > (sizePongX - this.getWidth())) {
                animateX(sizePongX - this.getWidth(), -this.getSpeedX());
            }
            if (this.getPositionY() > (sizePongY - this.getHeight())) {
                animateY(sizePongY - this.getHeight(), -this.getSpeedY());
            }
        }
        this.setPosition((this.getPositionX() + this.getSpeedX()), (this.getPositionY() + this.getSpeedY()));

        this.setPositionRectangle(this.getPositionX(), this.getPositionY());
    }

    @Override
    public boolean notCheating(int x, int y, int speedX,int speedY){
        return Math.abs(this.getPositionX() - x) <=BALL_SPEED*2 &&
                Math.abs(this.getPositionY() - y) <=BALL_SPEED *2&&
                Math.abs(this.getSpeedX() - speedX) <=BALL_SPEED*2 &&
                Math.abs(this.getSpeedY() - speedY) <=BALL_SPEED*2;
    }

    public int getLosePlayerSize(){
        if (this.getPositionX() <= 0) {
            return 1;
        }
        else if (this.getPositionY()<= 0) {
            return 3;
        }
        else if (this.getPositionX() >= (Pong.getSizePongX() - this.getWidth())) {
            return 2;
        }
        else if (this.getPositionY() >= (Pong.getSizePongY() - this.getHeight())) {
            return 4;
        }
        return 0;
    }

    public int restart(PongItem loseRacket){
        //int a = RandomNumber.randomValue(1,4);
        switch(loseRacket.getNumber()){
            case 1:
                this.setPosition(loseRacket.getPositionX()+loseRacket.getWidth(),
                        loseRacket.getPositionY() + loseRacket.getHeight()/2);
                break;
            case 2:
                this.setPosition(loseRacket.getPositionX() - this.getWidth(),
                        loseRacket.getPositionY() + loseRacket.getHeight()/2);
                break;
            case 3:
                this.setPosition(loseRacket.getPositionX() + this.getWidth(),
                        loseRacket.getPositionY() + loseRacket.getHeight());
                break;
            case 4:
                this.setPosition(loseRacket.getPositionX() + loseRacket.getWidth() / 2,
                        loseRacket.getPositionY() - this.getHeight());
                break;
        }

        return loseRacket.getNumber();
    }

    public void launch(int idRacket){
        int newSpeedX, newSpeedY;
        switch (idRacket){
            case 1:
                newSpeedX = BALL_SPEED;
                newSpeedY = RandomNumber.randomValue(-BALL_SPEED, BALL_SPEED);
                break;
            case 2:
                newSpeedX = -BALL_SPEED;
                newSpeedY = RandomNumber.randomValue(-BALL_SPEED,BALL_SPEED);
                break;
            case 3:
                newSpeedX = RandomNumber.randomValue(-BALL_SPEED, BALL_SPEED);
                newSpeedY = BALL_SPEED;
                break;
            case 4:
                newSpeedX = RandomNumber.randomValue(-BALL_SPEED,BALL_SPEED);
                newSpeedY = -BALL_SPEED;
                break;
            default:
                newSpeedX = 0;
                newSpeedY = 0;
        }
        this.setSpeed(newSpeedX, newSpeedY);
        this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());

    }

    public boolean collision(PongItem pi){
        if (this.getSpeedX() == 0) {
            this.setSpeedX(BALL_SPEED);
        }
        if (this.getSpeedY() == 0)
            this.setSpeedY(BALL_SPEED);

        Rectangle ball = new Rectangle(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY(), this.getWidth(), this.getHeight());

//        if(pi instanceof Bonus){
//            Bonus bonus = (Bonus) pi;
//            if(bonus.getNumber() == 4){
//                Rock r =(Rock) bonus;
//                if (ball.intersects(r.getRock())){
//                    r.setLifePointRock(r.getLifePointRock() - 1);
//                    if(r.getLifePointRock() == 0)
//                        bonus.stopBonus();
//                    doCollisionBall(bonus);
//                    return true;
//                }
//                return false;
//            }
//        }
//        else {
           if ((ball.getX() >= pi.getSurface().getX() + pi.getSurface().getWidth())      // trop à droite
                    || (ball.getX() + ball.getWidth() <= pi.getSurface().getX()) // trop à gauche
                    || (ball.getY() >= pi.getSurface().getY() + pi.getSurface().getHeight()) // trop en bas
                    || (ball.getY() + ball.getHeight() <= pi.getSurface().getY())) { // trop en haut
                return false;
            } else {
                if (pi instanceof Racket)
                    doCollision(pi);
                else
                    doCollisionBall(pi);
                return true;
            }
//        }
//        return false;
    }

    public void doCollision(PongItem pi) {
        if (this.getPositionY() <= 0) {
            pi.setPosition(pi.getPositionX(), this.getPositionY() + this.getHeight() + pi.getSpeedX());
        } else if (this.getPositionY() + this.getHeight() >= Pong.getSizePongY()) {
            pi.setPosition(pi.getPositionX(), this.getPositionY() - pi.getHeight() - pi.getSpeedX());
        }
        if (pi instanceof Racket) {
            Racket r = (Racket) pi;
            if (r.getNumber() < 3) {
                if (this.getSurface().getX() >= pi.getSurface().getX() + pi.getSurface().getWidth() ||
                        this.getSurface().getX() + this.getSurface().getWidth() <= pi.getSurface().getX()) {

                    this.setSpeedX(-this.getSpeedX());
                    this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());
                } else {
                    this.setSpeedX(-this.getSpeedX());
                    this.setSpeedY(-this.getSpeedY());
                    int s = pi.getSpeedX();
                    this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY() + s);

                }
            }
            else{
                if(this.getSurface().getY() >= pi.getSurface().getY() + pi.getSurface().getHeight() ||
                        this.getSurface().getY() + this.getSurface().getHeight() <= pi.getSurface().getY() ){
                    this.setSpeedY(-this.getSpeedY());
                    this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());
                }
                else{
                    this.setSpeed(-this.getSpeedX(), -this.getSpeedY());
                    int s = pi.getSpeedY();
                    this.setPosition(this.getPositionX() + this.getSpeedX() + s, this.getPositionY() + this.getSpeedY());
                }
            }
        }
    }

    public void doCollisionBall(PongItem pi){
        if(! (pi instanceof Bonus )) {
            pi.setSpeed(pi.getSpeedX(), -pi.getSpeedY());
            this.setSpeed(this.getSpeedX(), -this.getSpeedY());
        }
    }


}
