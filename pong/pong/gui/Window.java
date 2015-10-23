package pong.gui;

import javax.swing.JFrame;
import java.awt.*;

/**
 * A Window is a Java frame containing an Pong
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Pong component to be displayed
	 */
	private final Pong pong;

	/**
	 * Width of pong area
	 */
	public static final int SIZE_PONG_X = 800;
	/**
	 * Height of pong area
	 */
	public static final int SIZE_PONG_Y = 600;

	/**
	 * Constructor
	 */
	public Window(Pong pong) {
		this.pong = pong;

		this.addKeyListener(pong.racket);
	}

	/**
	 * Displays the Window using the defined margins, and call the
	 * {@link Pong#animate()} method of the {@link Pong} every 100ms
	 */
	public void displayOnscreen() {
		add(pong);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		while(true) {
			pong.racket.animate(SIZE_PONG_X,SIZE_PONG_Y);
			pong.ball.animate(SIZE_PONG_X,SIZE_PONG_Y);
			pong.updateScreen();

			try {
				Thread.sleep(pong.timestep);
			} catch (InterruptedException e) {};
		}
	}
}
