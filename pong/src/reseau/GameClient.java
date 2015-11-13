package src.reseau;

import src.gui.Pong;
import src.gui.Racket;
import src.gui.Window;
import src.reseau.Player;

import java.io.*;
import java.nio.channels.SocketChannel;

/**
 * Nous passerons
 * 1 port du client
 * 2 adresse de l'autre joueur
 * 3 port de l'autre joueur
 */
public class GameClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = Integer.parseInt(args[0]);
        String adresse = "localhost";
        int portConnection = 7777;
        Pong pong = new Pong();

        Window window = new Window(pong);

        Player client = new Player(pong);
        client.initServeur(port);


        ((Racket) client.pong.pongList.get(0)).setIdPlayer(1);
        client.addPlayer();

        if (args.length > 1) {
            client.nombrePlayer = 1;
            client.connectionServer(adresse, portConnection, true);
            System.out.println("fin");
        }
        client.aff();
        window.displayOnscreen();
        while (true) {

            if (client.server != null) {
                SocketChannel sc = client.server.accept();
                if (sc != null) {
                    client.connectionAccept(sc);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (client.nombrePlayer > 1) {
                System.out.println("je lance la boucle");

//                client.getWriter(0).configureBlocking(false);

                System.out.println("je lance la boucle 1");


                String info = client.Information();

                //System.out.println(info);
                //if (is.available() != 0) {
                System.out.println("je lance la boucle 2");
                client.getWriter(0).socket().setTcpNoDelay(false);
                client.getWriter(0).configureBlocking(true);

                ObjectOutputStream  oos = new
                        ObjectOutputStream(client.getWriter(0).socket().getOutputStream());
                oos.writeObject(info);
                oos.flush();
                //client.getWriter(0).configureBlocking(true);

                client.update();

                client.getWriter(0).configureBlocking(false);
                client.pong.animateItem();
                try {
                    Thread.sleep(pong.timestep);
                } catch (InterruptedException e) {
                }
                ;
                //client.aff();
            }

        }
    }
}