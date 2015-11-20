package src;

import src.gui.PongItem;
import src.gui.Racket;
import src.gui.Window;
import src.gui.Pong;

/**
 * Starting point of the Pong application
 */
public class Main  {
	public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, InterruptedException {
			Pong pong = new Pong();
		pong.add(new Racket(3));
	Window window = new Window(pong);
	window.displayOnscreen();
		while(true){
			pong.animateItem();
			Thread.sleep(10);
		}
	}
}
