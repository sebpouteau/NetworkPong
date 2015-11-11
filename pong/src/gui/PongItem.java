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
    private Rectangle surface;

    public PongItem()
    {
        this.setPosition(new Point(10, 0));

    }

    public void animate(int sizePongX,int sizePongY){
    }

    public void setSurface(int x, int y, int width, int height){
        this.surface = new Rectangle(x, y,width,height);
    }

    public Rectangle getSurface(){
        return this.surface;
    }

    public Rectangle setPositionRectangle (int x, int y){
        return this.surface = new Rectangle(x, y, this.getWidth(), this.getHeight());
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

    /*  public static boolean collision (ArrayList<PongItem> p){
          boolean t = false;
          for (int i = 0; i < p.size(); i++) {
              for (int j = 0; j < p.size() ; j++) {
                  if(p.get(i) instanceof Ball && i!=j){
                      Ball b = (Ball) p.get(i);
                      b.collision(p.get(j));
                      t = true;
                  }
              }
          }
          return t;
      }*/
    public boolean collision (ArrayList<PongItem> p){
        boolean t = false;
        for (int i = 0; i < p.size() ; i++) {

            if(this instanceof Ball && p.get(i) != this){
                Ball b = (Ball) this;
                b.collision(p.get(i));
                t = true;
            }
         /*   if (this instanceof Racket && p.get(i) != this && p.get(i) instanceof Ball){
                Racket r =(Racket) this;
                r.collision(p.get(i));
                t = true;
            }*/

        }
        return t;
    }

}
