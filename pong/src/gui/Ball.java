package src.gui;

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
        super();
        ImageIcon icon;
        this.setImageItem(Toolkit.getDefaultToolkit().createImage(
                ClassLoader.getSystemResource("image/ball.png")));
        this.setPosition(40,40);
        icon = new ImageIcon(this.getImageItem());
        this.setWidth(icon.getIconWidth());
        this.setHeight(icon.getIconHeight());
        this.setSpeed(new Point(BALL_SPEED, BALL_SPEED));
        this.setSurface(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight());
    }

    public Ball(int x, int y) {
        super();
        this.setPosition(x,y);
        ImageIcon icon;
        this.setImageItem(Toolkit.getDefaultToolkit().createImage(
                ClassLoader.getSystemResource("image/ball.png")));
        icon = new ImageIcon(this.getImageItem());
        this.setWidth(icon.getIconWidth());
        this.setHeight(icon.getIconHeight());
        this.setSpeed(new Point(BALL_SPEED, BALL_SPEED));
        this.setSurface(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight());
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
    public boolean collision(PongItem pi){

        if(this.getSurface().getBounds().intersects(pi.getSurface().getBounds())){

            if(((pi.getSurface().intersectsLine(this.getSurface().getMinX(), this.getSurface().getMinY(), this.getSurface().getMinX(), this.getSurface().getMaxY())) ||
                    (pi.getSurface().intersectsLine(this.getSurface().getMaxX(), this.getSurface().getMinY(), this.getSurface().getMaxX(), this.getSurface().getMaxY())))){

                if( this.getSurface().getMaxY() > pi.getSurface().getMinY() && this.getSurface().getMinY() < pi.getSurface().getMaxY() &&
                        this.getSurface().getMaxY() < pi.getSurface().getMaxY() && this.getSurface().getMinY() > pi.getSurface().getMinY()){

                    this.setSpeedX(-this.getSpeedX());
                    this.setPositionX(this.getPositionX() + this.getSpeedX());
                }

                else {
                    this.setSpeedY(-this.getSpeedY());
                    this.setSpeedX(- this.getSpeedX());
                    this.setPositionY(this.getPositionY() + pi.getSpeedY());
                    this.setPositionX(this.getPositionX() + this.getSpeedX());
                }
            }

            else{

                this.setSpeedY(-this.getSpeedY());
                this.setPositionY(this.getPositionY() + this.getSpeedY());
            }

            if(this.getPositionY() <= 0 ){
                pi.setPosition(pi.getPositionX(), this.getPositionY() + this.getHeight() + 1);
            }
            else if(this.getPositionY() + this.getHeight() >= 600){
                pi.setPosition(pi.getPositionX() , this.getPositionY() - pi.getHeight() - 1);
            }

            if(this.getSurface().getBounds().intersects(pi.getSurface().getBounds())){
                this.setPosition(this.getPositionX() , this.getPositionY() + pi.getSpeedX());

            }

            return true;
        }else {
            return false;
        }
    }

//    public boolean collision(PongItem pi){
//        this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());
//        if((this.getSurface().getX() >= pi.getSurface().getX() + pi.getSurface().getWidth())      // trop à droite
//                || (this.getSurface().getX() + this.getSurface().getWidth() <= pi.getSurface().getX()) // trop à gauche
//                || (this.getSurface().getY() >= pi.getSurface().getY() + pi.getSurface().getHeight()) // trop en bas
//                || (this.getSurface().getY() + this.getSurface().getHeight() <= pi.getSurface().getY())) { // trop en haut
//            this.setPosition(this.getPositionX() - this.getSpeedX(), this.getPositionY() - this.getSpeedY());
//            return false;
//        }
//        else {
//
//            doCollision(pi);
//            return true;
//        }
//    }
//    public void doCollision(PongItem pi) {
//        boolean done = false;
//        if(this.getSurface().getY() > 0 || this.getSurface().getMaxY() < 600) {
//            if (this.getSurface().getY() <= pi.getSurface().getY() + pi.getSurface().getHeight() || this.getSurface().getY() + this.getSurface().getHeight() <= pi.getSurface().getY()) {
//                this.setPosition(this.getPositionX() - this.getSpeedX(), this.getPositionY() - this.getSpeedY());
//                done = true;
//                this.setSpeedY(-this.getSpeedY());
//                int s = pi.getSpeedX();
//                this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY() + s);
//
//            } else if (this.getSurface().getX() <= pi.getSurface().getX() + pi.getSurface().getWidth() || this.getSurface().getX() + this.getSurface().getWidth() >= pi.getSurface().getX()) {
//                if(!done)
//                    this.setPosition(this.getPositionX() - this.getSpeedX(), this.getPositionY() - this.getSpeedY());
//                this.setSpeedX(-this.getSpeedX());
//                this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY()+ this.getSpeedY() );
//            }
//        }
//        else
//            pi.setSpeedX(0);
//    }


}
