package src.gui;

import java.awt.event.KeyEvent;

public class Racket extends PongItem {
	private static final int RACKET_SPEED = 4;
	private String image = "image/raquette.png";
	private String imageH = "image/raquetteH.png";

	private String getImage(){return image;}
	private String getImageH(){return imageH;}

	public Racket(int idPlayer){
		super();
		if(idPlayer < 3) {
			initImage(getImage());
			if(idPlayer == 1)
				this.setPosition(10 ,Pong.getSizePongY()/2 - getHeight()/2 );
			else
				this.setPosition(Pong.getSizePongX() - getWidth() - 10, Pong.getSizePongY()/2- getHeight()/2);
		}
		else {
			initImage(getImageH());
			if(idPlayer == 3)
				this.setPosition( Pong.getSizePongX() / 2 - this.getWidth()/2, 10);
			else
				this.setPosition(Pong.getSizePongX() / 2 - this.getWidth()/2, Pong.getSizePongY() - getHeight() - 10);
		}
		this.setNumber(idPlayer);
	}

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

	public void collision(PongItem pi){
		if (this.getSurface().intersects(pi.getSurface())){
            if(pi instanceof Bonus){
                Bonus b = (Bonus) pi;
                b.disappear();
                b.duration(this);
            }
        }

	}

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
