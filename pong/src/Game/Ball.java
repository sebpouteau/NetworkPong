package src.Game;

import src.util.RandomNumber;
import java.awt.*;

public class Ball extends PongItem {

    public static final int BALL_SPEED = 4;
    private String image = "image/ball.png";

    public Ball(int id) {
        super(Pong.getSizePongX()/2, Pong.getSizePongY()/2);
        this.setNumber(id);
        initImage(image);
        int speedX =RandomNumber.randomValue(-BALL_SPEED, BALL_SPEED);
        int speedY =RandomNumber.randomValue(-BALL_SPEED, BALL_SPEED);
        this.setSpeed(new Point(speedX,speedY));
    }

    /* =================================================
                      Functions
       ================================================= */

    /**
     * Modifie la position et la vitesse de la balle en X suivant les parametres.
     * @param setPos Nouvelle position en X.
     * @param setSpeed Nouvelle vitesse en X.
     */
    private void animateX(int setPos, int setSpeed) {
        this.setPositionX(setPos);
        this.setSpeedX(setSpeed);
    }

    /**
     * Modifie la position et la vitesse de la balle en Y suivant les parametres.
     * @param setPos Nouvelle position en Y.
     * @param setSpeed Nouvelle vitesse en Y.
     */
    private void animateY(int setPos, int setSpeed) {
        this.setPositionY(setPos);
        this.setSpeedY(setSpeed);
    }

    @Override
    public void animate(int sizePongX,int sizePongY) {
        if (Pong.getIfStart()) {
            this.setSpeed(0, 0);
        }
        else {
            if (this.getPositionX() < 0) {
                animateX(0, -this.getSpeedX());
            }
            if (this.getPositionY() < 0) {
                animateY(0, -this.getSpeedY());
            }
            if (this.getPositionX() > (sizePongX - this.getWidth())) {
                animateX(sizePongX - this.getWidth(), -this.getSpeedX());
            }
            if (this.getPositionY() > (sizePongY - this.getHeight())) {
                animateY(sizePongY - this.getHeight(), -this.getSpeedY());
            }
        }
        this.setPosition((this.getPositionX() + this.getSpeedX()), (this.getPositionY() + this.getSpeedY()));
        this.setPositionRectangle(this.getPositionX(), this.getPositionY());
    }

    @Override
    public boolean notCheating(int x, int y, int speedX, int speedY) {
        boolean t = Math.abs(this.getPositionX() - x) <= BALL_SPEED * BALL_SPEED &&
                Math.abs(this.getPositionY() - y) <= BALL_SPEED * BALL_SPEED;
        boolean s = Math.abs(this.getSpeedX() - speedX) <= BALL_SPEED * 2 &&
                Math.abs(this.getSpeedY() - speedY) <=BALL_SPEED * 2;
        return s && t;
    }

    /**
     * Regarde si la balle touche un des cotes de l'ecran et renvoit le numero du joueur correspondant.
     * @return Un entier representant un cote de l'écran. Si il represente un joeur en jeu, il perd la balle.
     */
    public int losePlayerSize() {
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

    /**
     * Positionne la balle sur la raquette du joueur qui vient de la perdre.
     * @param loseRacket La raquette du joueur qui a perdu la balle.
     * @return Le numéro du joueur qui a perdu la balle.
     */
    public int restart(PongItem loseRacket) {
        switch(loseRacket.getNumber()) {
            case 1:
                this.setPosition(loseRacket.getPositionX() + loseRacket.getWidth(),
                        loseRacket.getPositionY() + loseRacket.getHeight()/2 - this.getHeight()/2);
                break;
            case 2:
                this.setPosition(loseRacket.getPositionX() - this.getWidth(),
                        loseRacket.getPositionY() + loseRacket.getHeight()/2 - this.getHeight()/2);
                break;
            case 3:
                this.setPosition(loseRacket.getPositionX() + this.getWidth()/2 - this.getWidth()/2,
                        loseRacket.getPositionY() + loseRacket.getHeight());
                break;
            case 4:
                this.setPosition(loseRacket.getPositionX() + loseRacket.getWidth()/2 - this.getWidth()/2,
                        loseRacket.getPositionY() - this.getHeight());
                break;
        }
        this.setSpeed(0,0);
        return loseRacket.getNumber();
    }

    /**
     * Donne une vitesse aleatoire a la balle de maniere a ce qu'elle soit remise en jeu
     * avec plus de possibilites pour sa trajectoire.
     * @param idRacket La raquette qui tient la balle.
     */
    public void launch(int idRacket) {
        int newSpeedX, newSpeedY;
        switch (idRacket) {
            case 1:
                newSpeedX = BALL_SPEED;
                newSpeedY = RandomNumber.randomValue(-BALL_SPEED, BALL_SPEED);
                break;
            case 2:
                newSpeedX = -BALL_SPEED;
                newSpeedY = RandomNumber.randomValue(-BALL_SPEED,BALL_SPEED);
                break;
            case 3:
                newSpeedX = RandomNumber.randomValue(-BALL_SPEED, BALL_SPEED);
                newSpeedY = BALL_SPEED;
                break;
            case 4:
                newSpeedX = RandomNumber.randomValue(-BALL_SPEED,BALL_SPEED);
                newSpeedY = -BALL_SPEED;
                break;
            default:
                newSpeedX = 0;
                newSpeedY = 0;
        }
        this.setSpeed(newSpeedX, newSpeedY);
        this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());
    }

    /**
     * Teste si la balle est rentree en collision avec un PongItem.
     * Verifie si la vitesse de la balle est nulle et si vrai, lui redonne une vitesse .
     * @param item Le PongItem avec lequel on teste la collision de la balle.
     * @return True si il y a une collision, False sinon.
     */
    public boolean collision(PongItem item) {
        if (this.getSpeedX() == 0) {
            this.setSpeedX(BALL_SPEED);
        }
        if (this.getSpeedY() == 0) {
            this.setSpeedY(BALL_SPEED);
        }
        Rectangle ball = new Rectangle(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY(), this.getWidth(), this.getHeight());

        if ((ball.getX() >= item.getSurface().getX() + item.getSurface().getWidth())
                || (ball.getX() + ball.getWidth() <= item.getSurface().getX())
                || (ball.getY() >= item.getSurface().getY() + item.getSurface().getHeight())
                || (ball.getY() + ball.getHeight() <= item.getSurface().getY())) {
            return false;
        } else {
            if (item instanceof Racket) {
                doCollision(item);
            }
            else {
                doCollisionBall(item);
            }
            return true;
        }
    }

    /**
     * Le "rebond", les modifications de vitesse a effectuer suivant ou la balle est en collision avec le PongItem.
     * @param item PongItem avec lequelle il y a la collision (seulement raquette pour cette version).
     */
    public void doCollision(PongItem item) {
        if (this.getPositionY() <= 0) {
            item.setPosition(item.getPositionX(), this.getPositionY() + this.getHeight() + item.getSpeedX());
        } else if (this.getPositionY() + this.getHeight() >= Pong.getSizePongY()) {
            item.setPosition(item.getPositionX(), this.getPositionY() - item.getHeight() - item.getSpeedX());
        }
        if (item.getNumber() < 3) {
            if (this.getSurface().getX() >= item.getSurface().getX() + item.getSurface().getWidth() ||
                    this.getSurface().getX() + this.getSurface().getWidth() <= item.getSurface().getX()) {
                this.setSpeedX(-this.getSpeedX());
                this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());
            } else {
                this.setSpeedX(-this.getSpeedX());
                this.setSpeedY(-this.getSpeedY());
                int s = item.getSpeedX();
                this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY() + s);
            }
        }
        else{
            if (this.getSurface().getY() >= item.getSurface().getY() + item.getSurface().getHeight() ||
                    this.getSurface().getY() + this.getSurface().getHeight() <= item.getSurface().getY()) {
                this.setSpeedY(-this.getSpeedY());
                this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());
            }
            else {
                this.setSpeed(-this.getSpeedX(), -this.getSpeedY());
                int s = item.getSpeedY();
                this.setPosition(this.getPositionX() + this.getSpeedX() + s, this.getPositionY() + this.getSpeedY());
            }
        }
    }

    /**
     * Le changement de trajectoire de la balle si elle en rencontre une autre.
     * @param item Un PongItem representant une balle.
     */
    public void doCollisionBall(PongItem item){
        if (!(item instanceof Bonus)) {
            item.setSpeed(item.getSpeedX(), -item.getSpeedY());
            this.setSpeed(this.getSpeedX(), -this.getSpeedY());
        }
    }
}
