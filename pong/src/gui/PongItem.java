package src.gui;

import javax.swing.*;
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
    private Point speed;
    private int number;

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
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
    public void setPosition(int x,int y) {
        this.position.setLocation(x, y);
        this.setPositionRectangle(x,y);
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

    public PongItem() {
        this.setPosition(new Point(10, 0));
        this.speed = new Point(0,0);
    }

    public PongItem(int x, int y) {
        this.setPosition(new Point(x, y));
        this.speed = new Point(0 , 0);
    }

    /**
     * Charge l'image qui se trouve en "chemin" et initialise sa surface et position
     * @param chemin le chemin permettant d'accéder à l'image
     */
    public void initImage(String chemin){
        this.setImageItem(Toolkit.getDefaultToolkit().createImage(
                ClassLoader.getSystemResource(chemin)));
        this.setSurface(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight());
    }

    /**
     * Appelle animate plus précisement pour chaque type réel des objets
     * @param sizePongX longueur de la fenêtre
     * @param sizePongY largueur de la fenêtre
     */
    public void animate(int sizePongX,int sizePongY){
    }

    /**
     * fonction verifiant si les donnée sont valide
     * @param x nouvelle position en X
     * @param y nouvelle position en Y
     * @param speedX nouvelle vitesse en X
     * @param speedY nouvelle vitesse en Y
     * @return true si valide, false sinon
     */
    public boolean notCheating(int x, int y, int speedX,int speedY){
        return true;
    }

    /**
     * Test si l'objet appelant cette fonction est d'un type ou d'un autre et lance plus précisement la recherche et gestion des collisions
     * @param p liste des PongItem composants le pong
     * @return vrai si une collision a eu lieu et faux sinon
     */
    public boolean collision (ArrayList<PongItem> p){
        boolean t = false;
        for (int i = 0; i < p.size() ; i++) {

            if(this instanceof Ball && p.get(i) != this){
                Ball b = (Ball) this;
                b.collision(p.get(i));
                t = true;
            }
            if(this instanceof Bonus && p.get(i) instanceof Racket ){
                Racket r = (Racket) p.get(i);
                r.collision(this);

            }
        }
        return t;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) { }

}
