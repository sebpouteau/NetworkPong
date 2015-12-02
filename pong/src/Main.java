package src;

import src.Network.*;
import src.gui.*;

import java.io.*;
import java.nio.channels.SocketChannel;

/**
 * Nous passerons pour le premier:
 *                                1 nombre joueur
 * deuxieme :
 *             1 adresse de l'autre joueur
 *             2 port de l'autre joueur
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
					System.out.println("accepte");
					client.connectionAcceptPlayer(sc.socket());
				}
			}
		}
		System.out.println(client.getIdplayer());
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
			for (int i = 0; i < client.listSocketSize(); i++) {
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
			for (int i = 0; i < client.listSocketSize(); i++) {
				client.update(i);
			}

			/* animation du jeu */
			client.getPong().animateItem();


			client.attributionScore();
		}
	}


}
