package src.gui;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by sebpouteau on 06/11/15.
 */
public class GameClient2player extends JFrame {
    private Pong pong;
    ServerSocketChannel server;
    Socket socket;
    int port;
    int nombrePlayer = 0;

    public GameClient2player(Pong pong) {
        this.pong = pong;
    }

    private String SendAllItem() {
        StringBuffer message = new StringBuffer();
        message.append(this.nombrePlayer + " " +
                pong.pongList.get(pong.pongList.size() - 1).getPositionX() + " " +
                pong.pongList.get(pong.pongList.size() - 1).getPositionX());
        message.append(";");
        for (int i = 0; i < pong.pongList.size() - 1; i++) {
            if (pong.pongList.get(i) instanceof Ball) {
                message.append("BA " + pong.pongList.get(i).getPositionX() + " " + pong.pongList.get(i).getPositionY());
            }
            if (pong.pongList.get(i) instanceof Racket) {
                message.append("RAK " +
                        ((Racket) pong.pongList.get(i)).getIdPlayer() + " " +
                        pong.pongList.get(i).getPositionX() + " " + pong.pongList.get(i).getPositionY());
            }
            //message.append();
            message.append(";");
        }
        return message.toString();
    }

    public void addNewClient(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        PrintStream ps = new PrintStream(os, false, "utf-8");
        System.out.println("nouveau player");
        String lu = "";
        while (true) {
            String tmp = br.readLine();
            if (tmp.compareTo("FIN") == 0)
                break;
            lu = tmp;
            System.out.println(lu);
        }
        System.out.println("fin lu");
        String[] message = lu.split(";");
        if (message.length != 2 && message[0].compareTo("Pong Play") != 0)
            return;
        String item = SendAllItem();
        ps.println(item);
        ps.println("FIN");
        System.out.println("fin lu");
        String[] port = message[1].split(":");
        this.port = Integer.parseInt(port[1]);

    }

    public void addPlayer() {
        this.nombrePlayer++;

    }

    public void init(String message) {
        String[] decoupe = message.split(";");
        for (int i = 0; i < decoupe.length - 1; i++) {
            String[] de = decoupe[i].split(" ");
            if (de[0].length() == 1) {
                ((Racket) (pong.pongList.get(0))).setIdPlayer(Integer.parseInt(de[1]));
                pong.pongList.get(0).setPositionX(Integer.parseInt(de[1]));
                pong.pongList.get(0).setPositionY(Integer.parseInt(de[2]));
            }
            if (de[0].compareTo("BA") == 0) {
                pong.pongList.get(1).setPositionX(Integer.parseInt(de[1]));
                pong.pongList.get(1).setPositionY(Integer.parseInt(de[2]));
            }
            if (de[0].compareTo("RAK") == 0) {
                pong.pongList.add(0,new Racket(Integer.parseInt(de[1]),
                        Integer.parseInt(de[2]), Integer.parseInt(de[3])));
            }
        }

    }


    /**
     * Nous passerons
     * 1 port du client
     * 2 adresse de l'autre joueur
     * 3 port de l'autre joueur
     */
    public static void main(String[] args) throws IOException {
        int port = 7777;
        String adresse = "localhost";
        int portConnection = 7777;
        Pong pong = new Pong();

        Window window = new Window(pong);

        GameClient2player client = new GameClient2player(pong);
        if (args.length == 0) {
            client.server = ServerSocketChannel.open();
            client.server.socket().bind(new InetSocketAddress(port));
            client.server.configureBlocking(false);
            client.addPlayer();
        } else {
            client.nombrePlayer = 2;
            portConnection = Integer.parseInt(args[1]);
            adresse = args[0];
            Socket player = new Socket(adresse, portConnection);
            InputStream is = player.getInputStream();
            OutputStream os = player.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            PrintStream ps = new PrintStream(os, false, "utf-8");
            ps.println("Pong Play;Port:7778");
            ps.flush();
            ps.println("FIN");
            ps.flush();
            if (is.available() != 0) {
                String lu = br.readLine();
                if (lu != null)
                    System.out.println(lu);
            }
            System.out.println("j'zi fini d'envoyer");
            String lu = "";
            while (true) {
                String tmp = br.readLine();
                if (tmp.compareTo("FIN") == 0)
                    break;
                lu = tmp;
                System.out.println(lu);
            }
            client.init(lu);

        }

        window.displayOnscreen();
        while (true) {

            if (client.server != null) {
                SocketChannel sc = client.server.accept();
                if (sc != null) {
                    client.pong.add(new Racket(2, 50, 50));
                    client.addNewClient(sc.socket());
                    client.nombrePlayer++;
                }
            }
            if (client.nombrePlayer > 1) {
                pong.animateItem();
                try {
                    Thread.sleep(pong.timestep);
                } catch (InterruptedException e) {
                }
                ;
                //client.aff();
            }

        }
    }


public void aff(){
    for (int i= 0; i < pong.pongList.size();i++){
        System.out.println(pong.pongList.get(i).getPosition());
}
}

}




