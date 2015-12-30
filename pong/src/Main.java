package src;

import src.Network.*;
import src.gui.*;

import java.io.*;
import java.nio.channels.SocketChannel;

/**
 * Classe permettant de lancer le jeu.
 */
public class Main{
	
	private static int PORT = 7777;

	public static void main(String[] args) throws IOException, InterruptedException {
		int port = PORT;
		Pong pong = new Pong();
		Player client = new Player(pong);
		client.addPlayer();

		/* Initialisation du Serveur avec un port */
		boolean serverOK = false;
		while (!serverOK) {
			try {
				client.initServeur(port);
				serverOK = true;
			} catch (IOException e) {
				port++;
			}
		}
		
		Menu menu = new Menu(client);
		menu.displayMenu();
		while (menu.isAllPlayerConnect()){
		}
		menu.endMenu();

		pong.setTabScore(client.getMaxPlayer());
		String namePlayer = "Joueur ";
		namePlayer += client.getIdplayer();
		Window window = new Window(pong, namePlayer);
		window.displayOnscreen();

		/* Boucle de jeu quand tous les joueurs sont connectés */
		while (true) {
			if (client.getNumberPlayer()< 2) {
				pong.updateScreenEnd();
				return;
			}
			/* Envoie des informations aux autres joueurs */
			String info = client.information();
			for (int i = 0; i < client.getListSocketSize(); i++) {
				client.sendMessage(client.getSocketPlayer(i), info);
			}
			try {
				Thread.sleep(Pong.timestep);
			} catch (InterruptedException ignored) {}
			
			/* Reception des information des autres joueurs */
			for (int i = 0; i < client.getListSocketSize(); i++) {
				client.update(i);
			}

			client.getPong().animateItems();
			client.attributionScore();
		}
	}


}
