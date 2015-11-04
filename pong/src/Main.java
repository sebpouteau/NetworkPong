package src;

import src.gui.Window;
import src.gui.Pong;
import src.reseau.ClientServeur;

import java.net.Socket;

/**
 * Starting point of the Pong application
 */
public class Main  {

	public static void main(String[] args) {
		Pong pong = new Pong();
		Socket client;
		if(args.length == 0){
			ClientServeur t = new ClientServeur();
			t.start();
		}
			System.out.println(args.length);

		Window window = new Window(pong);
		window.displayOnscreen();
	}

}
