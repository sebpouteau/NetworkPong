package src.gui;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Racket extends PongItem {
	private static final int RACKET_SPEED = 4;
	private static int HEIGHT = 80 ;
	private static int WIDTH = 15;

	public Racket(int idPlayer){
		super();
		if(idPlayer < 3) {

			if(idPlayer == 1) {
				this.setSurface(10, Pong.getSizePongY() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
			}
			else
				this.setSurface(Pong.getSizePongX() - WIDTH - 10, Pong.getSizePongY()/2- HEIGHT/2, WIDTH, HEIGHT);
		}
		else {
			if(idPlayer == 3)
				this.setSurface( Pong.getSizePongX() / 2 - HEIGHT/2, 10, HEIGHT, WIDTH);
			else
				this.setSurface(Pong.getSizePongX() / 2 - HEIGHT/2, Pong.getSizePongY() - WIDTH - 10, HEIGHT,WIDTH);
		}
		this.setNumber(idPlayer);
	}

	/**
	 * Déplace la raquette suivant sa vitesse
	 * @param sizePongX longuer de la fenêtre
	 * @param sizePongY largeur de la fenêtre
	 */
	public void animate(int sizePongX,int sizePongY){
		/* Update racket position */
		this.setPositionX(this.getPositionX()+this.getSpeedY());
		this.setPositionY(this.getPositionY() + this.getSpeedX());
		if (this.getPositionX() < 0)
			this.setPositionX(0);
		if (this.getPositionX() > sizePongX - this.getWidth())
			this.setPositionX(sizePongX - this.getWidth());
		if (this.getPositionY() < 0)
			this.setPositionY(0);
		if (this.getPositionY() > sizePongY - this.getHeight())
			this.setPositionY(sizePongY - this.getHeight());
		this.setPositionRectangle(this.getPositionX(), this.getPositionY());

	}

	/**
	 * Pour vérifier si une collision a lieu avec un Bonus de manière à lancer le Bonus sur cette raquette
	 * @param pi le Bonus
	 */
	public void collision(PongItem pi){
		if (this.getSurface().intersects(pi.getSurface())){
            if(pi instanceof Bonus){
                Bonus b = (Bonus) pi;
                if(b.isVisible()) {
                    b.disappear();
                    b.startBonus(this);
                }
            }
       }

	}

	/**
	 * Change la vitesse de la raquette suivant le type de raquette (verticale ou horizontale) et la touche appuchée
	 * @param e événement créer lorsque qu'une touche et enfoncé permet de changer la vitesse de la raquette suivant la touche enfoncée
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				if(this.getNumber() < 3)
					this.setSpeedX(-RACKET_SPEED);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				if(this.getNumber() < 3)
					this.setSpeedX(RACKET_SPEED);
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				if(this.getNumber() > 2)
					this.setSpeedY(-RACKET_SPEED);
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				if(this.getNumber() > 2)
					this.setSpeedY(RACKET_SPEED);
			default:
				//System.out.println("got press "+e);
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				this.setSpeedX(0);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				this.setSpeedX(0);
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				this.setSpeedY(0);
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				this.setSpeedY(0);
			default:
				//System.out.println("got release "+e);
		}
	}
	public void keyTyped(KeyEvent e) { }


}
