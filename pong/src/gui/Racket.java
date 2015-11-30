package src.gui;

import java.awt.event.KeyEvent;

public class Racket extends PongItem {
	private static final int RACKET_SPEED = 6;
	private static int HEIGHT = 80 ;
	private static int WIDTH = 15;

	public Racket(int idPlayer){
		super();
		if(idPlayer < 3)
			if (idPlayer == 1) {
				this.setSurface(10, Pong.getSizePongY() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
			} else {
				this.setSurface(Pong.getSizePongX() - WIDTH - 10, Pong.getSizePongY() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
			}
		else {
			if(idPlayer == 3)
				this.setSurface( Pong.getSizePongX() / 2 - HEIGHT/2, 10, HEIGHT, WIDTH);
			else
				this.setSurface(Pong.getSizePongX() / 2 - HEIGHT/2, Pong.getSizePongY() - WIDTH - 10, HEIGHT,WIDTH);
		}
		this.setNumber(idPlayer);
	}

	@Override
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


	@Override
	public boolean notCheating(int x, int y, int speedX,int speedY){
		return Math.abs(this.getPositionX() - x) <=RACKET_SPEED &&
				Math.abs(this.getPositionY() - y) <=RACKET_SPEED &&
				Math.abs(this.getSpeedX() - speedX) <=RACKET_SPEED &&
				Math.abs(this.getSpeedY() - speedY) <=RACKET_SPEED;
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

	@Override
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
				break;
		}
	}

	@Override
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
		}
	}

	@Override
	public void keyTyped(KeyEvent e) { }


}
