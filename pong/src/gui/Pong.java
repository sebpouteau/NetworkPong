package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * An Pong is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Pong extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Width of src area
	 */
	private static final int SIZE_PONG_X = 800;
	/**
	 * Height of src area
	 */
	private static final int SIZE_PONG_Y = 600;

	/**
	/**
	 * Constant (c.f. final) common to all Pong instances (c.f. static)
	 * defining the background color of the Pong
	 */
	private static final Color backgroundColor = new Color(209, 209, 206);


	/**
	 * Time step of the simulation (in ms)
	 */
	public static final int timestep = 10;
	/**
	 * Pixel data buffer for the Pong rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;


	public Racket racket;
	public Ball ball;
	public PongItem pongItems[];

	public Pong() {
		racket = new Racket();
		ball = new Ball();
		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(racket);
	}

	public void animateItem() {
		ball.collision(racket);
		ball.animate(SIZE_PONG_X, SIZE_PONG_Y);
		racket.animate(SIZE_PONG_X, SIZE_PONG_Y);

		this.updateScreen();
	}

	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

	public void updateScreen() {
		if (buffer == null) {
			/* First time we get called with all windows initialized */
			buffer = createImage(SIZE_PONG_X,SIZE_PONG_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}
		/* Fill the area with blue */
		graphicContext.setColor(backgroundColor);
		graphicContext.fillRect(0, 0, SIZE_PONG_X, SIZE_PONG_Y);

		/* Draw items */
		graphicContext.drawImage(ball.getImageItem(), ball.getPositionX(), ball.getPositionY(), ball.getWidth(), ball.getHeight(), null);
		graphicContext.drawImage(racket.getImageItem(),
						         racket.getPositionX(), racket.getPositionY(),
				                 racket.getWidth(), racket.getHeight(),
				                 null);
		this.repaint();
	}
}
