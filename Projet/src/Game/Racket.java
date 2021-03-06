package src.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Racket extends PongItem implements KeyListener {

	private static final int RACKET_SPEED = 4;
	private static int HEIGHT = 80 ;
	private static int WIDTH = 15;
	private int speedRacket;

	/**
	 * Initialise la raquette. La positionne, lui attribue un numero, une vitesse.
	 * Si elle a le numero 1 ou 2, elle sera verticale sinon horizontael, dans ce cas on inverse HEIGHT et WIDTH pour la dessiner.
	 * @param idPlayer Numero du joueur.
	 */
	public Racket(int idPlayer) {
		super();
		if (idPlayer < 3) {
			if (idPlayer == 1) {
				this.setSurface(10, Pong.getSizePongY()/2 - HEIGHT/2, WIDTH, HEIGHT);
			}
			else {
				this.setSurface(Pong.getSizePongX() - WIDTH - 10, Pong.getSizePongY()/2 - HEIGHT/2, WIDTH, HEIGHT);
			}
		}
		else {
			if (idPlayer == 3) {
				this.setSurface(Pong.getSizePongX()/2 - HEIGHT/2, 10, HEIGHT, WIDTH);
			}
			else {
				this.setSurface(Pong.getSizePongX()/2 - HEIGHT/2, Pong.getSizePongY() - WIDTH - 10, HEIGHT, WIDTH);
			}
		}
		this.setNumber(idPlayer);
		this.speedRacket = RACKET_SPEED;
	}

    /* =================================================
                      Getter and Setter
       ================================================= */

	public int getSpeedRacket() {
		return speedRacket;
	}

	public void setSpeedRacket(int speed) {
		speedRacket = speed;
	}

    /* =================================================
                      Functions
       ================================================= */

	@Override
	public void animate(int sizePongX,int sizePongY){
		this.setPositionX(this.getPositionX()+this.getSpeedY());
		this.setPositionY(this.getPositionY() + this.getSpeedX());
		if (this.getPositionX() < 0) {
			this.setPositionX(0);
		}
		if (this.getPositionX() > sizePongX - this.getWidth()) {
			this.setPositionX(sizePongX - this.getWidth());
		}
		if (this.getPositionY() < 0) {
			this.setPositionY(0);
		}
		if (this.getPositionY() > sizePongY - this.getHeight()) {
			this.setPositionY(sizePongY - this.getHeight());
		}
		this.setPositionRectangle(this.getPositionX(), this.getPositionY());
	}

	@Override
	public boolean notCheating(int x, int y, int speedX, int speedY) {
		return Math.abs(this.getPositionX() - x) <= getSpeedRacket() * getSpeedRacket() &&
				Math.abs(this.getPositionY() - y) <= getSpeedRacket() * getSpeedRacket() &&
				Math.abs(this.getSpeedX() - speedX) <= getSpeedRacket() &&
				Math.abs(this.getSpeedY() - speedY) <= getSpeedRacket();
	}

	/**
	 * Verifie si une collision a lieu avec un Bonus de maniere a lancer le Bonus sur cette raquette.
	 * @param item Le Bonus.
	 */
	public void collision(PongItem item) {
		if (this.getSurface().intersects(item.getSurface())) {
			if (item instanceof Bonus) {
				Bonus b = (Bonus) item;
				if (b.isVisible()) {
					b.disappear();
					b.startBonus(this);
				}
			}
		}
	}

	/**
	 * Change la vitesse de la raquette suivant l'orientation de la raquette (verticale ou horizontale) et la touche appuyee.
	 * @param e Evenement cree lorsque qu'une touche et enfonce permet de changer la vitesse de la raquette suivant la touche.
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				if (this.getNumber() < 3) {
					this.setSpeedX(-getSpeedRacket());
				}
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				if (this.getNumber() < 3) {
					this.setSpeedX(getSpeedRacket());
				}
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				if (this.getNumber() > 2) {
					this.setSpeedY(-getSpeedRacket());
				}
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				if (this.getNumber() > 2) {
					this.setSpeedY(getSpeedRacket());
				}
				break;
		}
	}

	/**
	 * Remet la vitesse de la raquette à 0. Si on relache "SPACE" et que l'on detient la balle, cela la relance.
	 * @param e Touche relachee du clavier (touches possibles: fleches directionnels et barre espace).
	 */
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
				break;
			case KeyEvent.VK_SPACE:
				if (Pong.getIfStart() && Pong.getWaitPlayer() == this.getNumber()) {
					Pong.setIfGo(true);
				}
				break;
		}
	}

	public void keyTyped(KeyEvent e) {}
}
