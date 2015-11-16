package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Ball extends PongItem {

    public static final int BALL_SPEED = 2;


    public Ball() {
        super();
        ImageIcon icon;
        this.setImageItem(Toolkit.getDefaultToolkit().createImage(
                ClassLoader.getSystemResource("image/ball.png")));
        this.setPosition(40,40);
        this.setSpeed(BALL_SPEED, BALL_SPEED);
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
        this.setSpeed(BALL_SPEED, BALL_SPEED);
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

            if(this.getPositionY() == 0 ){
                pi.setPosition(pi.getPositionX(), this.getPositionY() + this.getHeight() + 1);
            }
            else if(this.getPositionY() + this.getHeight() ==  600){
                pi.setPosition(pi.getPositionX() , this.getPositionY() - 1);
            }

            if(this.getSurface().getBounds().intersects(pi.getSurface().getBounds())){
                this.setPosition(this.getPositionX() , this.getPositionY() + pi.getSpeedX());

            }

            return true;
        }else {
            return false;
        }
    }

}
