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
        //while(!menu.getCreateGame() || !menu.getJoinGame())
        /* comportement du premier joueur */
//            if (menu.getCreateGame()) {
//                System.out.println("je créer un pong");
//                System.out.println(menu.getCreateGame());
//                client.getPong().add(new Racket(1));
//                client.getPong().add(new Ball(1, 80, 80));
//                client.getPong().add(new Bonus());
//                client.setMaxPlayer(Integer.parseInt(menu.getInfoCreate()));
//                //client.setMaxPlayer(Integer.parseInt(args[0]));
//            }

		/* comportement des autres joueur */
//            if (menu.getJoinGame()) {
//                System.out.println("je rejoins un pong");
//                //	String adress = args[0];
//                String adress = menu.getInfoJoin()[0];
//                int portConnection = Integer.parseInt(menu.getInfoJoin()[1]);
//                client.setNombrePlayer(1);
//                client.connectionServerInit(adress, portConnection, true);
//                client.getPong().addKeyListener(client.getMyRacket());
//            }


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
		menu.endMenu();
		Window window = new Window(pong);
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
					Thread.sleep(Pong.timestep);
				} catch (InterruptedException ignored) {
				}
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
