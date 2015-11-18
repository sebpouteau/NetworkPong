package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Racket extends PongItem {
	public static final int RACKET_SPEED = 4;


	public Racket(int idPlayer){
		super();
		ImageIcon icon;
		this.setImageItem(Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource("image/raquette.png")));

		icon = new ImageIcon(this.getImageItem());
		this.setWidth(icon.getIconWidth());
		this.setHeight(icon.getIconHeight());
	}
	public Racket(int idPlayer,int x, int y){
		super(x,y);
		this.setNumber(idPlayer);
		ImageIcon icon;
		this.setImageItem(Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource("image/raquette.png")));

		icon = new ImageIcon(this.getImageItem());
		this.setWidth(icon.getIconWidth());
		this.setHeight(icon.getIconHeight());
		this.setSurface(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight());
	}


	public void animate(int sizePongX,int sizePongY){
/* Update racket position */
		this.setPositionY(this.getPositionY() + this.getSpeedX());
		if (this.getPositionY() < 0)
			this.setPositionY(0);
		if (this.getPositionY() > sizePongY - this.getHeight())
			this.setPositionY(sizePongY - this.getHeight());
		this.setPositionRectangle(this.getPositionX(), this.getPositionY());

	}


	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				this.setSpeedX( -RACKET_SPEED);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				this.setSpeedX(RACKET_SPEED);
				break;
			default:
				System.out.println("got press "+e);
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
			default:
				System.out.println("got release "+e);
		}
	}
	public void keyTyped(KeyEvent e) { }


}
