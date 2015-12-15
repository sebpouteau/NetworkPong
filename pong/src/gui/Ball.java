package src.gui;

import src.util.RandomNumber;
import java.awt.*;


public class Ball extends PongItem {

    public static final int BALL_SPEED = 4;
    private String image = "image/ball.png";

    public Ball(int id) {
        super(Pong.getSizePongX()/2,Pong.getSizePongY()/2);
        this.setNumber(id);
        initImage(image);
        this.setSpeed(new Point(BALL_SPEED, BALL_SPEED));
    }

    private void animateX(int setPos, int setSpeed){
        this.setPositionX(setPos);
        this.setSpeedX(setSpeed);
    }

    private void animateY(int setPos, int setSpeed){
        this.setPositionY(setPos);
        this.setSpeedY(setSpeed);
    }

    @Override
    public void animate(int sizePongX,int sizePongY){
        if(Pong.getIfStart()) {
            this.setSpeed(0, 0);
        }
        else{
		/* Update ball position */
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
        System.out.println(this.getSpeed());
    }

    @Override
    public boolean notCheating(int x, int y, int speedX,int speedY){
        boolean t=  Math.abs(this.getPositionX() - x) <=BALL_SPEED*BALL_SPEED &&
                Math.abs(this.getPositionY() - y) <=BALL_SPEED *BALL_SPEED;
        boolean s = Math.abs(this.getSpeedX() - speedX) <=BALL_SPEED*2 &&
                Math.abs(this.getSpeedY() - speedY) <=BALL_SPEED*2;
        if (!t){
            System.out.println("pos beug" + this.getPositionX() +" "+ x + " " +this.getPositionY() +" "+ y );
        }
        if (!s)
            System.out.println("vitesse beug");
        return s && t;
    }

    /**
     * Regarde si la balle touche un des côtés de l'écran et renvoit le numéro du joueur correspondant
     * @return un entier représentant un côté de l'écran qui si il y a un joueur, il a perdu.
     */
    public int getLosePlayerSize(){
        if (this.getPositionX() <= 0) {
            System.out.println("le jouer 1 a perdu");
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
     * Positionne la balle sur la raquette du joueur qui vient de la perdre
     * @param loseRacket la raquette du joueur qui a perdu la balle
     * @ le numéro du joueur qui a perdu
     */
    public int restart(PongItem loseRacket){
        switch(loseRacket.getNumber()){
            case 1:
                this.setPosition(loseRacket.getPositionX()+loseRacket.getWidth(),
                        loseRacket.getPositionY() + loseRacket.getHeight()/2);
                break;
            case 2:
                this.setPosition(loseRacket.getPositionX() - this.getWidth(),
                        loseRacket.getPositionY() + loseRacket.getHeight()/2);
                break;
            case 3:
                this.setPosition(loseRacket.getPositionX() + this.getWidth(),
                        loseRacket.getPositionY() + loseRacket.getHeight());
                break;
            case 4:
                this.setPosition(loseRacket.getPositionX() + loseRacket.getWidth() / 2,
                        loseRacket.getPositionY() - this.getHeight());
                break;
        }
        this.setSpeed(0,0);
        return loseRacket.getNumber();
    }

    /**
     * Donne une vitesse aléatoire à la balle de manière à ce qu'elle soit remise en jeu avec une meilleure amplitude de possibilités
     * @param idRacket La raquette qui tient la balle.
     */
    public void launch(int idRacket){
        int newSpeedX, newSpeedY;
        switch (idRacket){
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
     * Vérifie si la vitesse de la balle est nulle et lui redonne une vitesse, mais principalement teste si la ball est rentrée en collision avec item.
     * @param item Le PongItem avec lequel on teste la collision de la balle.
     * @return vrai si il y a une collision, faux sinon
     */
    public boolean collision(PongItem item){
        if (this.getSpeedX() == 0) {
            this.setSpeedX(BALL_SPEED);
        }
        if (this.getSpeedY() == 0)
            this.setSpeedY(BALL_SPEED);

        Rectangle ball = new Rectangle(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY(), this.getWidth(), this.getHeight());

//        if(item instanceof Bonus){
//            Bonus bonus = (Bonus) item;
//            if(bonus.getNumber() == 4){
//                Rock r =(Rock) bonus;
//                if (ball.intersects(r.getRock())){
//                    r.setLifePointRock(r.getLifePointRock() - 1);
//                    if(r.getLifePointRock() == 0)
//                        bonus.stopBonus();
//                    doCollisionBall(bonus);
//                    return true;
//                }
//                return false;
//            }
//        }
//        else {
           if ((ball.getX() >= item.getSurface().getX() + item.getSurface().getWidth())      // trop à droite
                    || (ball.getX() + ball.getWidth() <= item.getSurface().getX()) // trop à gauche
                    || (ball.getY() >= item.getSurface().getY() + item.getSurface().getHeight()) // trop en bas
                    || (ball.getY() + ball.getHeight() <= item.getSurface().getY())) { // trop en haut
                return false;
            } else {
                if (item instanceof Racket)
                    doCollision(item);
                else
                    doCollisionBall(item);
                return true;
            }
//        }
//        return false;
    }

    /**
     * Le "rebond", les modifications de vitesse a effectuer suivant où la balle est en collision avec le pongItem
     * @param item PongItem avec lequelle il y a la collision (seulement raquette pour cette version)
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
            if(this.getSurface().getY() >= item.getSurface().getY() + item.getSurface().getHeight() ||
                    this.getSurface().getY() + this.getSurface().getHeight() <= item.getSurface().getY() ){
                this.setSpeedY(-this.getSpeedY());
                this.setPosition(this.getPositionX() + this.getSpeedX(), this.getPositionY() + this.getSpeedY());
            }
            else{
                this.setSpeed(-this.getSpeedX(), -this.getSpeedY());
                int s = item.getSpeedY();
                this.setPosition(this.getPositionX() + this.getSpeedX() + s, this.getPositionY() + this.getSpeedY());
            }
        }
    }

    public void doCollisionBall(PongItem item){
        if(! (item instanceof Bonus )) {
            item.setSpeed(item.getSpeedX(), -item.getSpeedY());
            this.setSpeed(this.getSpeedX(), -this.getSpeedY());
        }
    }


}
