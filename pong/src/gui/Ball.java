package src.gui;

import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Ball extends PongItem {

    public static final int BALL_SPEED = 5;
    private Point speed;
    private String image = "image/ball.png";

    public Point getSpeed() {
        return speed;
    }
    public int getSpeedX() {
        return (int)speed.getX();
    }
    public int getSpeedY() {
        return (int)speed.getY();
    }
    public void setSpeed(int x, int y) {
        this.speed.setLocation(x, y);
    }
    public void setSpeedX(int x) {
        this.getSpeed().setLocation(x, this.getSpeed().getY());
    }
    public void setSpeedY(int y) {
        this.getSpeed().setLocation(this.getSpeed().getX(), y);
    }
    public void setSpeed(Point speed) {
        this.speed = speed;
    }
    public String getImage(){return image;}

    public Ball() {
        super(40,40);
        initImage(getImage());
        this.setSpeed(new Point(BALL_SPEED, BALL_SPEED));
    }

    public Ball(int id, int x, int y) {
        super(x,y);
        this.setNumber(id);
        initImage(getImage());
        this.setSpeed(new Point(BALL_SPEED, BALL_SPEED));
    }

    public void animate(int sizePongX,int sizePongY){
        this.setPosition((this.getPositionX() + this.getSpeedX()),(this.getPositionY() + this.getSpeedY()));
		/* Update ball position */
        if (this.getPositionX() < 0) {
            this.setPositionX(0);
            this.setSpeedX(-this.getSpeedX());
        }
        if (this.getPositionY()< 0) {
            this.setPositionY(0);
            this.setSpeedY(-this.getSpeedY());
        }
        if (this.getPositionX() > (sizePongX - this.getWidth())) {
            this.setPositionX(sizePongX - this.getWidth());
            this.setSpeedX(-this.getSpeedX());
        }
        if (this.getPositionY() > (sizePongY - this.getHeight())) {
            this.setPositionY(sizePongY - this.getHeight());
            this.setSpeedY(-this.getSpeedY());
        }
        this.setPositionRectangle(this.getPositionX(), this.getPositionY());

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


    public void restart(){
        //à completer si l'on souhaite changer position /vitesse
        this.setPosition(Pong.getSizePongX()/2,Pong.getSizePongY()/2);
    }

    public boolean collision(PongItem pi){
        if (this.getSpeedX() == 0) {
            this.setSpeedX(BALL_SPEED);
        }
        if(this.getSpeedY() == 0)
            this.setSpeedY(BALL_SPEED);
        Rectangle ball = new Rectangle(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY(), this.getWidth(), this.getHeight());
        if((ball.getX() >= pi.getSurface().getX() + pi.getSurface().getWidth())      // trop à droite
                || (ball.getX() + ball.getWidth() <= pi.getSurface().getX()) // trop à gauche
                || (ball.getY() >= pi.getSurface().getY() + pi.getSurface().getHeight()) // trop en bas
                || (ball.getY() + ball.getHeight() <= pi.getSurface().getY())) { // trop en haut
            return false;
        }
        else {
            if(pi instanceof Ball)
                doCollisionBall(pi);
            else
                doCollision(pi);
            return true;
        }
    }

    public void doCollision(PongItem pi) {

        if(this.getPositionY() <= 0 ){
            pi.setPosition(pi.getPositionX(), this.getPositionY() + this.getHeight() + pi.getSpeedX());
        }
        else if(this.getPositionY() + this.getHeight() >= Pong.getSizePongY()){
            pi.setPosition(pi.getPositionX() , this.getPositionY() - pi.getHeight() - pi.getSpeedX());
        }
        if (this.getSurface().getX() >= pi.getSurface().getX() + pi.getSurface().getWidth() || this.getSurface().getX() + this.getSurface().getWidth() <= pi.getSurface().getX()) {

            this.setSpeedX(-this.getSpeedX());
            this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY()+ this.getSpeedY() );
        }
        else{
            this.setSpeedY(-this.getSpeedY());
            int s = pi.getSpeedX();
            this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY() + s);

        }
    }

    public void doCollisionBall(PongItem pi){

        this.setSpeed(this.getSpeedX(), -this.getSpeedY());
        pi.setSpeed(pi.getSpeedX(), -pi.getSpeedY());
    }


}
