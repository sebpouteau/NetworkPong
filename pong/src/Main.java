package src;

import src.Network.*;
import src.gui.*;

import java.awt.event.WindowAdapter;
import java.io.*;
import java.nio.channels.SocketChannel;


/**
 * Nous passerons pour le premier: 1 port
 *                                2 nombre joueur
 * deuxieme :  1 port du client
 *             2 adresse de l'autre joueur
 *             3 port de l'autre joueur
 */
public class Main{
	public static void main(String[] args) throws IOException, InterruptedException {
		int port = Integer.parseInt(args[0]);
		Pong pong = new Pong();
		Player client = new Player(pong);
		client.initServeur(port);
		client.addPlayer();

		if (args.length == 2){
			client.getPong().add(new Racket(1));
			client.getPong().add(new Ball(1, 80, 80));
			client.setMaxPlayer(Integer.parseInt(args[1]));
		}
		if (args.length > 2) {
			String adresse = args[1];
			int portConnection = Integer.parseInt(args[2]);
			client.setNombrePlayer(1);
			client.connectionServerInit(adresse, portConnection, true);
			client.getPong().addKeyListener(client.getMyRacket());
		}

		while (client.getNombrePlayer() != client.getMaxPlayer() ) {
			if (client.getServer() != null) {
				SocketChannel sc = client.getServer().accept();
				if (sc != null) {
					sc.socket().setTcpNoDelay(true);
					client.connectionAcceptPlayer(sc.socket());
				}
			}
		}
		Window window = new Window(pong);
		window.displayOnscreen();

		for (int i=0; i< client.listSocketSize();i++){
			System.out.println(client.getSocketPlayer(i).getPort() + " "+ client.getSocketPlayer(i).getNumeroPlayer());
		}
		while (true) {
			if (client.getNombrePlayer()< 2)
				return;
			if (client.getNombrePlayer() > 1) {
				String info = client.information();
				for (int i = 0; i < client.listSocketSize(); i++) {
					client.sendMessage(client.getSocketPlayer(i), info);
					try {
						Thread.sleep(Pong.timestep);
					} catch (InterruptedException ignored) {
					}
				}
				for (int i = 0; i < client.listSocketSize(); i++) {
						client.update(i);
				}
				client.getPong().animateItem();

			}
			client.getScore();
		}
	}


}
