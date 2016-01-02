package src.gui;

import javax.swing.*;

/**
 * A Window is a Java frame containing an Pong
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private final Pong pong;

	public Window(Pong pong, String namePlayer) {
		super(namePlayer);
		this.pong = pong;
		this.addKeyListener((Racket)pong.getItem(0));
	}

	/**
	 * Displays the Window using the defined margins, and call the
	 * {@link Pong} method of the {@link Pong} every 100ms
	 */
	public void displayOnscreen() {
		add(pong);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.setResizable(false);
	}
}
