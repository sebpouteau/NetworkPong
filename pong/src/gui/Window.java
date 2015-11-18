package src.gui;

import javax.swing.JFrame;

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
		this.pong = pong;

		for (int i = 0; i < pong.listItemSize(); i++) {
			if (pong.getItem(i) instanceof Racket) {
				this.addKeyListener(pong.getItem(i));

			}

		}
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
