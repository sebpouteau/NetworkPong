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

		/* Initialisation du Serveur avec une port */
		boolean serverOK = false;
		while (!serverOK) {
			try {
				client.initServeur(port);
				serverOK=true;
			} catch (IOException e) {
				port++;
			}
		}
		Menu menu = new Menu(client);

		/* Boucle de connection entre tout les joueurs */
		while (client.getNombrePlayer() != client.getMaxPlayer() ) {
			if (client.getServer() != null) {
				SocketChannel sc = client.getServer().accept();
				if (sc != null) {
					sc.socket().setTcpNoDelay(true);
					client.connectionAcceptPlayer(sc.socket());
				}
			}
		}
		pong.setTabScore(client.getMaxPlayer());
		menu.endWait();
		String namePlayer = "Joueur ";
		namePlayer += client.getIdplayer();
		Window window = new Window(pong, namePlayer);
		window.displayOnscreen();

		/* Boucle de jeu une fois que tout les joueur sont connecté */
		while (true) {
			if (client.getNombrePlayer()< 2)
				return;
			/* Envoie des information au autres joueurs */

			String info = client.information();
			for (int i = 0; i < client.getListSocketSize(); i++) {
				client.sendMessage(client.getSocketPlayer(i), info);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
			}
			try {
				Thread.sleep(Pong.timestep);
			} catch (InterruptedException ignored) {
			}
			/* Reception des information des autres joueurs */
			for (int i = 0; i < client.getListSocketSize(); i++) {
				client.update(i);
			}

			/* animation du jeu */
			client.getPong().animateItem();


			client.attributionScore();
		}
	}


}
