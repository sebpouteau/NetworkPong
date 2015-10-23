package pong.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sebpouteau on 23/10/15.
 */

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
        icon = new ImageIcon(this.getImageItem());
        this.setWidth(icon.getIconWidth());
        this.setHeight(icon.getIconHeight());
        this.setSpeed(new Point(BALL_SPEED, BALL_SPEED));
    }

    public void animate(int sizePongX,int sizePongY, PongItem pi){
		/* Update ball position */
        this.getPosition().translate(this.getSpeedX(), this.getSpeedY());
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


    }
    public boolean collision(PongItem pi){
        if((this.getPositionX() <= pi.getPositionX() + pi.getWidth()) && (this.getPositionY() <= pi.getPositionY() + pi.getHeight()) && (this.getPositionY() >= pi.getPositionY() - pi.getHeight())){
            this.setSpeedX(-this.getSpeedX());
            this.setPositionX(this.getPositionX() + BALL_SPEED );
            this.setPositionY(this.getPositionY() + BALL_SPEED );
            return true;
        }else {
            return false;
        }
    }

}
