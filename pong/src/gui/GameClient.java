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
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        String adresse = "localhost";
        int portConnection = 7777;
        Pong pong = new Pong();
        Window window = new Window(pong);
        Player client = new Player(pong);
        client.initServeur(port);
        client.pong.getItem(0).setNumber(1);
        client.addPlayer();

        if (args.length > 1) {
            client.nombrePlayer = 1;
            client.connectionServerInit(adresse, portConnection, true);
        }
        window.displayOnscreen();
        while (true) {

            if (client.getServer() != null) {
                SocketChannel sc = client.getServer().accept();
                if (sc != null) {
                    client.connectionAccept(sc.socket());

                }
            }
            if (client.nombrePlayer > 1) {

                String info = client.Information();
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

                    client.update(i);
                }
                client.pong.animateItem();

            }

        }
    }
}