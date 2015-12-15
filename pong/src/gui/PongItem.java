package src.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PongItem {

    private int width;
    private int height;
    private int number;
    private Point position;
    private Image imageItem;
    private Rectangle surface;
    private Point speed;

    public PongItem() {
        this.setPosition(10, 0);
        this.speed = new Point(0,0);
    }

    /* =================================================
                      Getter and Setter
       ================================================= */

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Point getSpeed() {
        return speed;
    }

    public void setSpeed(int x, int y) {
        this.speed.setLocation(x, y);
    }

    public void setSpeed(Point speed) {
        this.speed = speed;
    }

    public int getSpeedX() {
        return (int)speed.getX();
    }

    public void setSpeedX(int x) {
        this.getSpeed().setLocation(x, this.getSpeed().getY());
    }

    public int getSpeedY() {
        return (int)speed.getY();
    }

    public void setSpeedY(int y) {
        this.getSpeed().setLocation(this.getSpeed().getX(), y);
    }

    public Image getImageItem() {
        return imageItem;
    }

    public void setImageItem(Image imageItem) {
        this.imageItem = imageItem;
        ImageIcon icon = new ImageIcon(imageItem);
        this.setWidth(icon.getIconWidth());
        this.setHeight(icon.getIconHeight());
    }

    public Rectangle getSurface(){
        return this.surface;
    }

    public void setSurface(int x, int y, int width, int height){
        this.surface = new Rectangle(x, y,width,height);
        this.setPosition(x, y);
        this.setWidth(width);
        this.setHeight(height);
    }

    public Rectangle setPositionRectangle (int x, int y){
        return this.surface = new Rectangle(x, y, this.getWidth(), this.getHeight());
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

    public int getPositionX() {
        return (int)position.getX();
    }

    public int getPositionY() {
        return (int)position.getY();
    }

    public void setPositionX(int x) {
        this.position.setLocation(x, this.getPositionY());
    }

    public void setPositionY(int y) {
        this.position.setLocation(this.getPositionX(), y);
    }

    public void setPosition(int x,int y) {
        this.position.setLocation(x, y);
        this.setPositionRectangle(x,y);
    }

    public PongItem(int x, int y) {
        this.setPosition(x, y);
        this.speed = new Point(0 , 0);
    }

    /* =================================================
                      Functions
       ================================================= */

    /**
     * Charge l'image qui se trouve en "chemin" et initialise sa surface et position.
     * @param chemin Le chemin permettant d'acceder a l'image.
     */
    public void initImage(String chemin){
        this.setImageItem(Toolkit.getDefaultToolkit().createImage(
                ClassLoader.getSystemResource(chemin)));
        this.setSurface(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight());
    }

    /**
     * Anime notre objet en fonction de la taille de la fenetre.
     * C'est-a-dire, le deplace suivant sa vitesse et à l'interieur de la fenetre.
     * @param sizePongX Longueur de la fenetre.
     * @param sizePongY Largueur de la fenêtre.
     */
    public void animate(int sizePongX,int sizePongY){}

    /**
     * Verifie si les donnees sont valides.
     * @param x Nouvelle position en X.
     * @param y Nouvelle position en Y.
     * @param speedX Nouvelle vitesse en X.
     * @param speedY Nouvelle vitesse en Y.
     * @return True si valide,False sinon.
     */
    public boolean notCheating(int x, int y, int speedX,int speedY){
        return true;
    }

    /**
     * Verifie et applique la collision si il y en a une avec un autre composant de la liste.
     * @param p Liste des PongItem composants le pong.
     * @return True si une collision a eu lieu et False sinon.
     */
    public boolean collision (ArrayList<PongItem> p){
        boolean t = false;
        for (PongItem aP : p) {
            if (this instanceof Ball && aP != this) {
                Ball b = (Ball) this;
                b.collision(aP);
                t = true;
            }
            if (this instanceof Bonus && aP instanceof Racket) {
                Racket r = (Racket) aP;
                r.collision(this);
            }
        }
        return t;
    }

}
