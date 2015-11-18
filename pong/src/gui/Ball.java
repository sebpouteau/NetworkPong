package src.gui;

import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Ball extends PongItem {

    public static final int BALL_SPEED = 2;
    private Point speed;

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

    public Ball() {
        super(40,40);
        this.initImage("image/ball.png");
        this.setSpeed(new Point(BALL_SPEED, BALL_SPEED));
    }

    public Ball(int id, int x, int y) {
        super(x,y);
        this.setNumber(id);
        this.initImage("image/ball.png");
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
//    public boolean collision(PongItem pi){
//        Rectangle ball =  new Rectangle( (int)this.getSurface().getX() + this.getSpeedX(),(int) this.getSurface().getY() + this.getSpeedY(), this.getWidth(), this.getHeight());
//        if(ball.getBounds().intersects(pi.getSurface().getBounds())){
//
//            if(((pi.getSurface().intersectsLine(ball.getMinX(), ball.getMinY(), ball.getMinX(), ball.getMaxY())) ||
//                    (pi.getSurface().intersectsLine(ball.getMaxX(), ball.getMinY(), ball.getMaxX(), ball.getMaxY())))){
//
//                if( ball.getMaxY() > pi.getSurface().getMinY() && ball.getMinY() < pi.getSurface().getMaxY() &&
//                        ball.getMaxY() < pi.getSurface().getMaxY() && ball.getMinY() > pi.getSurface().getMinY()){
//
//                    this.setSpeedX(-this.getSpeedX());
//                    if(this.getSpeedY() != this.getSpeedX() && this.getSpeedY() != -this.getSpeedX() )
//                        this.setSpeedY(this.getSpeedY()/2);
//                    //this.setPositionX(this.getPositionX() + this.getSpeedX());
//                }
//
//                else {
//                    if(this.getSpeedY() == this.getSpeedX() || this.getSpeedY() == -this.getSpeedX())
//                        this.setSpeedY(-this.getSpeedY()*2);
//                    else
//                        this.setSpeedY(-this.getSpeedY());
//                    this.setSpeedX(- this.getSpeedX());
//                    this.setPositionY(this.getPositionY() + pi.getSpeedY() + this.getSpeedY());
//                    this.setPositionX(this.getPositionX() + this.getSpeedX());
//                }
//            }
//
//            else{
//
//                this.setSpeedY(-this.getSpeedY());
//                this.setPositionY(this.getPositionY() + this.getSpeedY());
//            }
//
//            if(this.getPositionY() <= 0 ){
//                pi.setPosition(pi.getPositionX(), this.getPositionY() + this.getHeight() + 2);
//            }
//            else if(this.getPositionY() + this.getHeight() >= 600){
//                pi.setPosition(pi.getPositionX() , this.getPositionY() - pi.getHeight() - 2);
//            }
//
//            if(ball.getBounds().intersects(pi.getSurface().getBounds())){
//                this.setPosition(this.getPositionX() , this.getPositionY() + pi.getSpeedX());
//
//            }
//
//            return true;
//        }else {
//            return false;
//        }
//    }

    public boolean collision(PongItem pi){
        Rectangle ball = new Rectangle(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY(), this.getWidth(), this.getHeight());
        if((ball.getX() >= pi.getSurface().getX() + pi.getSurface().getWidth())      // trop à droite
                || (ball.getX() + ball.getWidth() <= pi.getSurface().getX()) // trop à gauche
                || (ball.getY() >= pi.getSurface().getY() + pi.getSurface().getHeight()) // trop en bas
                || (ball.getY() + ball.getHeight() <= pi.getSurface().getY())) { // trop en haut
            return false;
        }
        else {
            doCollision(pi);
            return true;
        }
    }
    public void doCollision(PongItem pi) {
        boolean done = false;

        if(this.getSurface().getY() > 0 || this.getSurface().getMaxY() < 600) {
            if (this.getSurface().getX() >= pi.getSurface().getX() + pi.getSurface().getWidth() || this.getSurface().getX() + this.getSurface().getWidth() <= pi.getSurface().getX()) {

                this.setSpeedX(-this.getSpeedX());
                this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY()+ this.getSpeedY() );
            }
            else if (this.getSurface().getY() <= pi.getSurface().getY() + pi.getSurface().getHeight() || this.getSurface().getY() + this.getSurface().getHeight() >= pi.getSurface().getY()) {
                this.setPosition(this.getPositionX() - this.getSpeedX(), this.getPositionY() - this.getSpeedY());
                done = true;
                this.setSpeedY(-this.getSpeedY());
                int s = pi.getSpeedX();
                this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY() + s);

            }
        }
        else
            pi.setSpeedX(0);
    }

}
