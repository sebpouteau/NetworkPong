package src.reseau;

import src.gui.Pong;
import src.gui.Racket;
import src.gui.Window;

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
                    client.connectionAccept(sc.socket());

                }
            }
            if (client.nombrePlayer > 1) {
//                client.getWriter(0).configureBlocking(false);

                String info = client.Information();
                //System.out.println(info);
                for (int i = 0; i < client.tabSocket.size(); i++) {
                client.getSocket(i).setTcpNoDelay(true);

                OutputStream os = client.getSocket(i).getOutputStream();
                PrintStream ps = new PrintStream(os, false, "utf-8");
                ps.println(info);
                ps.flush();




                    client.update(i);
                }//client.getWriter(0).configureBlocking(true);




                client.pong.animateItem();

                try {
                    Thread.sleep(Pong.timestep);
                } catch (InterruptedException e) {
                }
            }

        }
    }
}