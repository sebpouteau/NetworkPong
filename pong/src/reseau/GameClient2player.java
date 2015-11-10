package src.reseau;

import src.gui.Pong;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Created by sebpouteau on 06/11/15.
 */
public class GameClient2player {
    private Pong pong;
    ServerSocketChannel server;
    Socket socket;
    int port;
    int nombrePlayer;
    public GameClient2player(Pong pong) {
        this.pong = pong;
    }

    private String SendAllItem() {
        StringBuffer message = new StringBuffer();

            //message.append();
            message.append(";");

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
        //Ajout d'une racket a la liste
        System.out.println("fin lu");
        String[] port  = message[1].split(":");
        this.port = Integer.parseInt(port[1]);

    }

    public void addPlayer(){
        this.nombrePlayer++;

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
        int portConnection=7777;
        Pong pong = new Pong();
        GameClient2player client = new GameClient2player(pong);
        if (args.length == 0) {
            client.server = ServerSocketChannel.open();
            client.server.socket().bind(new InetSocketAddress(port));
            client.server.configureBlocking(false);
            client.addPlayer();
        }
        else{
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
            if (is.available() !=0) {
                String lu = br.readLine();
                if (lu == null)
                    break;
                System.out.println(lu);
            }

            System.out.println("j'zi fini d'envoyer");
        }


        while (true) {
            if (client.server !=null) {
                SocketChannel sc = client.server.accept();
                if (sc != null) {
                    client.addNewClient(sc.socket());
                }
            }
            else {


            }
        }


    }
}



