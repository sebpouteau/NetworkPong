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
        //this.setPosition(40,40);
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
//        while(pi.getSurface().contains(this.getSurface()))
        //          this.setPosition(this.getSpeedX(), this.getSpeedY());

        if(this.getSurface().getBounds().intersects(pi.getSurface().getBounds())){

            if(((pi.getSurface().intersectsLine(this.getSurface().getMinX(), this.getSurface().getMinY(), this.getSurface().getMinX(), this.getSurface().getMaxY())) ||
                    (pi.getSurface().intersectsLine(this.getSurface().getMaxX(), this.getSurface().getMinY(), this.getSurface().getMaxX(), this.getSurface().getMaxY())))){

                if( this.getSurface().getMaxY() > pi.getSurface().getMinY() && this.getSurface().getMinY() < pi.getSurface().getMaxY() &&
                        this.getSurface().getMaxY() < pi.getSurface().getMaxY() && this.getSurface().getMinY() > pi.getSurface().getMinY()){
                    this.setSpeedX(-this.getSpeedX());}
                // des qu'une collision est en cours je m'assure de replacer ma ball au limite de ma raquette puis je change sa vitesse
                else {
                    this.setSpeedY(-this.getSpeedY());
                    this.setSpeedX(- this.getSpeedX());
                }
            }

            else{

                this.setSpeedY(-this.getSpeedY());
            }
            //problème lors de l'arrivé à la verticale car le y ne change pas
            // this.setPositionX(this.getPositionX() + this.getSpeedX());
            //this.setPositionY(this.getPositionY() + this.getSpeedY());
            this.setPositionRectangle(this.getPositionX(), this.getPositionY());

            return true;
        }else {
            return false;
        }
    }

/*    public boolean collision(PongItem pi){
        if((this.getPositionX() <= pi.getPositionX() + pi.getWidth()) &&
                (this.getPositionY() <= pi.getPositionY() + pi.getHeight()) &&
                (this.getPositionY() >= pi.getPositionY() - pi.getHeight())){

            this.setSpeedX(-this.getSpeedX());

            this.setPositionX(this.getPositionX() + BALL_SPEED);
            this.setPositionY(this.getPositionY() + BALL_SPEED);
            return true;
        }else {
            return false;
        }
    }*/

}
