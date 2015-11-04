package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by sebpouteau on 23/10/15.
 */
public class Racket extends PongItem {
	public static final int RACKET_SPEED = 4;
	private int speed;

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Racket(){
		super();
		ImageIcon icon;
		this.setImageItem(Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource("image/racket.png")));
		icon = new ImageIcon(this.getImageItem());
		this.setWidth(icon.getIconWidth());
		this.setHeight(icon.getIconHeight());
	}

	public void animate(int sizePongX,int sizePongY){
/* Update racket position */
		this.setPositionY(this.getPositionY() + this.getSpeed());
		if (this.getPositionY() < 0)
			this.setPositionY(0);
		if (this.getPositionY() > sizePongY - this.getHeight()/2)
			this.setPositionY(sizePongY - this.getHeight()/2);
	}


	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				this.setSpeed( -RACKET_SPEED);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				this.setSpeed(RACKET_SPEED);
				break;
			default:
				System.out.println("got press "+e);
		}
	}
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				this.setSpeed(0);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				this.setSpeed(0);
				break;
			default:
				System.out.println("got release "+e);
		}
	}
	public void keyTyped(KeyEvent e) { }
}
