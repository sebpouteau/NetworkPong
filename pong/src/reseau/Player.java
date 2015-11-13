package src.reseau;

import src.gui.Ball;
import src.gui.Pong;
import src.gui.Racket;

import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Created by seb on 11/11/2015.
 */
public class Player {
    public Pong pong;
    public ServerSocketChannel server;
    public ArrayList<Socket> playerRead;
    public ArrayList<SocketChannel> playerWrite;
    public int port;
    public int idplayer;
    public int nombrePlayer;

    public int addReader(Socket socket) {
        this.playerRead.add(socket);
        return this.playerRead.size() - 1;
    }

    public int addWriter(SocketChannel socket) {
        this.playerWrite.add(socket);
        return this.playerWrite.size() - 1;

    }

    public SocketChannel getWriter(int pos) {
        return this.playerWrite.get(pos);
    }

    public Socket getReader(int pos) {
        return this.playerRead.get(pos);
    }


    public Player(Pong pong) {
        this.pong = pong;
        playerRead = new ArrayList<Socket>();
        playerWrite = new ArrayList<SocketChannel>();

    }

    private String SendAllItem() {
        StringBuffer message = new StringBuffer();
        message.append(this.nombrePlayer + " " + this.nombrePlayer + " " +
                pong.pongList.get(pong.pongList.size() - 1).getPositionX() + " " +
                pong.pongList.get(pong.pongList.size() - 1).getPositionX());
        message.append(";");
        for (int i = 0; i < pong.pongList.size() - 1; i++) {
            if (pong.pongList.get(i) instanceof Ball) {
                message.append("BALL ").append(pong.pongList.get(i).getPositionX()).append(" ").append(pong.pongList.get(i).getPositionY());
            }
            if (pong.pongList.get(i) instanceof Racket) {
                message.append("RACKET " +
                        ((Racket) pong.pongList.get(i)).getIdPlayer() + " " +
                        pong.pongList.get(i).getPositionX() + " " + pong.pongList.get(i).getPositionY());
            }
            //message.append();
            message.append(";");
        }
        return message.toString();
    }

    public void init(String message) {
        String[] decoupe = message.split(";");
        String[] de = decoupe[0].split(" ");
        this.nombrePlayer = Integer.parseInt(de[0]);
        this.idplayer = Integer.parseInt(de[1]);
        ((Racket) (pong.pongList.get(0))).setIdPlayer(this.idplayer);
        pong.pongList.get(0).setPositionX(Integer.parseInt(de[2]));
        pong.pongList.get(0).setPositionY(Integer.parseInt(de[3]));

        for (int i = 1; i < decoupe.length - 1; i++) {
            de = decoupe[i].split(" ");
            if (de[0].compareTo("BALL") == 0) {
                pong.pongList.get(1).setPositionX(Integer.parseInt(de[1]));
                pong.pongList.get(1).setPositionY(Integer.parseInt(de[2]));
            }
            if (de[0].compareTo("RACKET") == 0) {
                pong.pongList.add(new Racket(Integer.parseInt(de[1]),
                        Integer.parseInt(de[2]), Integer.parseInt(de[3])));
            }

        }

    }

    public void addPlayer() {
        this.nombrePlayer++;

    }

    public void addNewClient(SocketChannel socket) throws IOException {
        ObjectOutputStream  oos = new
                ObjectOutputStream(this.getWriter(0).socket().getOutputStream());
        System.out.println("nouveau player");

        String item = SendAllItem();
        System.out.println();
        oos.writeObject(item);
        oos.flush();
        oos.writeObject("FIN");
        oos.flush();
        System.out.println("fin");
    }

    public String Information() {
        StringBuffer message = new StringBuffer();
        message.append("RACKET " + ((Racket) pong.pongList.get(0)).getIdPlayer() + " " + pong.pongList.get(0).getPositionX() + " " + pong.pongList.get(0).getPositionY());
        message.append(";");
        message.append("BALL " + pong.pongList.get(1).getPositionX() + " " + pong.pongList.get(1).getPositionY());
        return message.toString();
    }

    public void update() throws IOException, ClassNotFoundException {
        ObjectInputStream ois =
                new ObjectInputStream(this.getWriter(0).socket().getInputStream());
        String message = (String) ois.readObject();
        if (message != null) {
            System.out.println(message);
            String[] item = message.split(";");
            for (int i = 0; i < item.length; i++) {
                String[] info = item[i].split(" ");
                int j = 0;
                if (info[j].compareTo("BALL") == 0) {
                    if (Integer.parseInt(info[j + 1]) > 400 && this.idplayer == 1)
                        pong.pongList.get(1).setPosition(Integer.parseInt(info[j + 1]), Integer.parseInt(info[j + 2]));
                    if (Integer.parseInt(info[j + 1]) < 400 && this.idplayer == 2)
                        pong.pongList.get(1).setPosition(Integer.parseInt(info[j + 1]), Integer.parseInt(info[j + 2]));
                }
                if (info[j].compareTo("RACKET") == 0) {
                    int idP = Integer.parseInt(info[j + 1]);
                    for (int k = 0; k < pong.pongList.size(); k++) {
                        if (pong.pongList.get(k) instanceof Racket) {
                            if (((Racket) pong.pongList.get(k)).getIdPlayer() == idP) {
                                pong.pongList.get(k).setPosition(Integer.parseInt(info[j + 2]), Integer.parseInt(info[j + 3]));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    public void initServeur(int port) throws IOException {
        this.port = port;
        this.server = ServerSocketChannel.open();
        this.server.socket().bind(new InetSocketAddress(this.port));
        this.server.configureBlocking(false);
    }

    public void connectionServer(String adress, int portConnection, boolean first) throws IOException, ClassNotFoundException {
        System.out.println("connection serveur");
        SocketChannel socket = SocketChannel.open();
        socket.socket().setTcpNoDelay(false);
        System.out.println("connection serveur 1");

        socket.connect(new InetSocketAddress(adress, portConnection));
        System.out.println("connection serveur 2");


        int position = this.addWriter(socket);
        System.out.println("connection serveur 3");


        ObjectOutputStream  oos = new
                ObjectOutputStream(socket.socket().getOutputStream());
        System.out.println("connection serveur 5");

        System.out.println(portConnection);
        oos.writeObject("Pong Play;Port: " + this.port + ";ConnectionFirst " + first);
        oos.flush();
        oos.writeObject("FIN");
        oos.flush();
        System.out.println("connection serveur 6");


        System.out.println("connection serveur 7");

        String info =read();
        System.out.println(info);
        this.init(info);
        getWriter(0).configureBlocking(false);
        //if (!first) {
//            this.pong.add(new Racket(2, 785, 0));
//            this.addPlayer();
//            this.addNewClient(this.getWriter(position));
        //}

    }


    public void aff() {
        for (int i = 0; i < pong.pongList.size(); i++) {
            System.out.println(pong.pongList.get(i).getPosition());
        }
    }

    public boolean validPlayer(String message) {
        String[] tabMessage = message.split(";");
        return tabMessage[0].compareTo("Pong Play") == 0 && tabMessage.length == 3;
    }

    public String decryptFirst(String message) {
        String[] tabMessage = message.split(";");
        String[] infoFirst = tabMessage[2].split(" ");
        return infoFirst[1];
    }

    public int decryptPort(String message) {
        String[] tabMessage = message.split(";");
        String[] infoPort = tabMessage[1].split(" ");
        return Integer.parseInt(infoPort[1]);

    }

    public String read() throws IOException, ClassNotFoundException {
        ObjectInputStream ois =
                new ObjectInputStream(getWriter(0).socket().getInputStream());
        String lu = "";
        while (true) {
            String tmp = (String)ois.readObject();
            if (tmp.compareTo("FIN") == 0)
                break;
            lu = tmp;
        }
        return lu;
    }

    public void connectionAccept(SocketChannel socket) throws IOException, ClassNotFoundException {
        int pos = this.addWriter(socket);

        String lu = read();
        System.out.println(lu);
        if (!validPlayer(lu))
            return;
        System.out.println(lu);
        int port = decryptPort(lu);
        String first = decryptFirst(lu);

        //String info = read(ois);

        //this.init(info);
        this.pong.add(new Racket(2, 785, 0));
        this.addPlayer();
        this.addNewClient(this.getWriter(pos));
        System.out.println(this.nombrePlayer);
getWriter(0).configureBlocking(false);


    }


}