package src.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PongItem implements KeyListener {
    private int width;
    private int height;
    private Point position;
    private Image imageItem;

    public PongItem()
    {
        this.setPosition(new Point(0, 0));

	}

    public void animate(int sizePongX,int sizePongY){

    }

    public Image getImageItem() {
        return imageItem;
    }

    public void setImageItem(Image imageItem) {
        this.imageItem = imageItem;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Point getPosition() {return (Point)position.clone();}
    public int getPositionX() {
        return (int)position.getX();
    }

    public int getPositionY() {
        return (int)position.getY();
    }

    public void setPosition(int x,int y) {
        this.position.setLocation(x, y);
    }
    public void setPositionX(int x) {
        this.position.setLocation(x, this.getPositionY());
    }
    public void setPositionY(int y) {
        this.position.setLocation(this.getPositionX(), y);
    }
    public void setPosition(Point position) {
        this.position = position;
    }


    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) { }

 /*   public boolean collision(PongItem pi){
        if((this.getPositionX() <= pi.getPositionX() + pi.getWidth()) &&
           (this.getPositionY() <= pi.getPositionY() + pi.getHeight()) &&
                (this.getPositionY() >= pi.getPositionY() - pi.getHeight())){
            return true;
        }else {
            return false;
        }
    }*/
    public static boolean collision (ArrayList<PongItem> p){
        boolean t = false;
        for (int i = 0; i < p.size() - 1; i++) {
            for (int j = i+1; j < p.size() ; j++) {
                if(p.get(i) instanceof Ball){
                    Ball b = (Ball) p.get(i);
                    b.collision(p.get(j));
                    t = true;
                }
            }
        }
        return t;
    }

}
