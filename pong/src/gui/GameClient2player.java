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
    /*private Pong pong;
    ServerSocketChannel server;
    Socket player;
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
                pong.pongList.add(new Racket(Integer.parseInt(de[1]),
                        Integer.parseInt(de[2]), Integer.parseInt(de[3])));
            }
        }

    }


    public String Information() {
        StringBuffer message = new StringBuffer();
        message.append("RACKET " + ((Racket)pong.pongList.get(0)).getIdPlayer() + " " + pong.pongList.get(0).getPositionX() + " " + pong.pongList.get(0).getPositionY());
        message.append(";");
        message.append("BALL " + pong.pongList.get(1).getPositionX() + " " + pong.pongList.get(1).getPositionY());
        return  message.toString();
    }

    public void update(String message){
        String[] item = message.split(";");
        for (int i = 0; i < item.length; i++) {
            String[] info = item[i].split(" ");
            int j = 0;
            if (info[j].compareTo("BALL") == 0){
                pong.pongList.get(1).setPosition(Integer.parseInt(info[j+1]), Integer.parseInt(info[j+2]));
            }
            if (info[j].compareTo("RACKET") == 0){
                int idP = Integer.parseInt(info[j+1]);
                for (int k = 0; k < pong.pongList.size(); k++) {
                    if (pong.pongList.get(k) instanceof Racket){
                        if (((Racket) pong.pongList.get(k)).getIdPlayer() == idP){
                            pong.pongList.get(k).setPosition(Integer.parseInt(info[j+2]), Integer.parseInt(info[j+3]));
                            break;
                        }
                    }
                }
            }
        }
    }*/


    /**
     * Nous passerons
     * 1 port du client
     * 2 adresse de l'autre joueur
     * 3 port de l'autre joueur
     */
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        String adresse = "localhost";
        int portConnection = 7777;
        Pong pong = new Pong();

        Window window = new Window(pong);

        Player client = new Player(pong);
        client.initServeur(port);


        ((Racket)client.pong.pongList.get(0)).setIdPlayer(1);
        client.addPlayer();

        if (args.length > 1){
            client.nombrePlayer = 1;
            client.connectionServer(adresse,portConnection,true);
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
                System.out.println(client.getReader(0).getPort() + " " + client.getWriter(0).getLocalPort());
                //System.out.println("je lance la boucle");
                client.pong.animateItem();
                InputStream is = client.getReader(0).getInputStream();
                OutputStream os = client.getWriter(0).getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                PrintStream ps = new PrintStream(os, false, "utf-8");
                String info = client.Information();
                //System.out.println(info);
                if (is.available() != 0) {
                    String lu = br.readLine();
                    if (lu != null)
                        System.out.println(lu);
                        client.update(lu);
                }
                ps.println(info);
                ps.flush();
                try {
                    Thread.sleep(pong.timestep);
                } catch (InterruptedException e) {
                }
                ;
                //client.aff();
            }

        }
    }

/*
    if (client.server != null) {
        SocketChannel sc = client.server.accept();
        if (sc != null) {
            client.player = sc.socket();
            client.pong.add(new Racket(2, 250, 250));
            client.nombrePlayer=2;
            client.addNewClient(sc.socket());
        }
    }
    if (client.nombrePlayer > 1) {
        //System.out.println("je lance la boucle");
        client.pong.animateItem();
        InputStream is = client.player.getInputStream();
        OutputStream os = client.player.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        PrintStream ps = new PrintStream(os, false, "utf-8");
        String info = client.Information();
        //System.out.println(info);
        if (is.available() != 0) {
            String lu = br.readLine();
            if (lu != null)
                //System.out.println(lu);
                client.update(lu);
        }
        ps.println(info);
        ps.flush();
        try {
            Thread.sleep(pong.timestep);
        } catch (InterruptedException e) {
        }
        ;
        //client.aff();
    }
*/
}






