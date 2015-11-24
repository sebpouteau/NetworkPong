package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

/**
 * A Window is a Java frame containing an Pong
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Pong component to be displayed
	 */
	private final Pong pong;

	 /* Constructor
	 */
	public Window(Pong pong) {
		super("Pong");
		this.pong = pong;
		this.addKeyListener(pong.getItem(0));


	}
	/**
	 * Displays the Window using the defined margins, and call the
	 * {@link Pong#()} method of the {@link Pong} every 100ms
	 */

	public void displayOnscreen() {
		add(pong);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		//while(true) {
			//pong.animateItem();

		//}
	}
}
