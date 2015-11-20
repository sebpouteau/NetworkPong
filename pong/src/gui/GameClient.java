package src.gui;

import java.io.*;
import java.nio.channels.SocketChannel;

/**
 * Nous passerons
 * 1 port du client
 * 2 adresse de l'autre joueur
 * 3 port de l'autre joueur
 */
public class GameClient {
    public static void main(String[] args) throws IOException, InterruptedException {
    int port = Integer.parseInt(args[1]);
        String adresse = "localhost";
        int portConnection = 7777;
        Pong pong = new Pong();
        Player client = new Player(pong);
        client.initServeur(port);
        client.addPlayer();

        if (args.length == 2){
            client.pong.add(new Racket(1));
            client.pong.add(new Ball(1, 80, 80));
        }
        if (args.length > 2) {
            client.nombrePlayer = 1;
            client.connectionServerInit(adresse, portConnection, true);
            client.pong.addKeyListener(client.pong.getItem(0));

        }

        int np = Integer.parseInt(args[0]);
        while (client.getNombrePlayer() != np ) {
            if (client.getServer() != null) {
                //System.out.println(client.getNombrePlayer() + np);
                SocketChannel sc = client.getServer().accept();
                if (sc != null) {
                    client.connectionAcceptPlayer(sc.socket());
                }
            }
        }
        Window window = new Window(pong);
        window.displayOnscreen();
        while (true) {



            if (client.nombrePlayer > 1) {
                String info = client.information();
                for (int i = 0; i < client.listSocketSize(); i++) {
                    client.getSocket(i).setTcpNoDelay(true);
                    OutputStream os = client.getSocket(i).getOutputStream();
                    PrintStream ps = new PrintStream(os, false, "utf-8");
                    ps.println(info);
                    ps.flush();
                    try {
                        Thread.sleep(Pong.timestep);
                    } catch (InterruptedException e) {
                    }
                }
                for (int i = 0; i < client.listSocketSize(); i++) {
                    client.update(i);
                }
                client.pong.animateItem();

            }
            client.getPoint();
        }
    }
}