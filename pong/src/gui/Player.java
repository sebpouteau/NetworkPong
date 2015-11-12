package src.gui;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;

/**
 * Created by seb on 11/11/2015.
 */
public class Player {
    Pong pong;
    ServerSocketChannel server;
    ArrayList<Socket> playerRead;
    ArrayList<Socket> playerWrite;
    int port;
    int idplayer;
    int nombrePlayer;

    public int addReader(Socket socket){
        this.playerRead.add(socket);
        return this.playerRead.size()-1;
    }

    public int addWriter(Socket socket){
        this.playerWrite.add(socket);
        return this.playerWrite.size()-1;

    }
    public Socket getWriter(int pos){
        return this.playerWrite.get(pos);
    }
    public Socket getReader(int pos){
        return this.playerRead.get(pos);
    }


    public Player(Pong pong) {
        this.pong = pong;
        playerRead = new ArrayList<Socket>();
        playerWrite = new ArrayList<Socket>();

    }

    private String SendAllItem() {
        StringBuffer message = new StringBuffer();
        message.append(this.nombrePlayer + " " + this.nombrePlayer + " " +
                pong.pongList.get(pong.pongList.size() - 1).getPositionX() + " " +
                pong.pongList.get(pong.pongList.size() - 1).getPositionX());
        message.append(";");
        for (int i = 0; i < pong.pongList.size() - 1; i++) {
            if (pong.pongList.get(i) instanceof Ball) {
                message.append("BALL " + pong.pongList.get(i).getPositionX() + " " + pong.pongList.get(i).getPositionY());
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

    public void addNewClient(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        PrintStream ps = new PrintStream(os, false, "utf-8");
        System.out.println("nouveau player");
        String item = SendAllItem();
        ps.println(item);
        ps.println("FIN");
    }

    public String Information() {
        StringBuffer message = new StringBuffer();
        message.append("RACKET " + ((Racket) pong.pongList.get(0)).getIdPlayer() + " " + pong.pongList.get(0).getPositionX() + " " + pong.pongList.get(0).getPositionY());
        message.append(";");
        message.append("BALL " + pong.pongList.get(1).getPositionX() + " " + pong.pongList.get(1).getPositionY());
        return message.toString();
    }

    public void update(String message) {
        String[] item = message.split(";");
        for (int i = 0; i < item.length; i++) {
            String[] info = item[i].split(" ");
            int j = 0;
            if (info[j].compareTo("BALL") == 0) {
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

    public void initServeur(int port) throws IOException {
        this.port = port;
        this.server = ServerSocketChannel.open();
        this.server.socket().bind(new InetSocketAddress(this.port));
        this.server.configureBlocking(false);
    }

    public void connectionServer(String adress, int portConnection,boolean first) throws IOException {
        int position = this.addWriter(new Socket(adress,portConnection));
        InputStream is = this.getWriter(position).getInputStream();
        OutputStream os = this.getWriter(position).getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        PrintStream ps = new PrintStream(os, false, "utf-8");
        ps.println("Pong Play;Port: "+this.port + ";ConnectionFirst "+ first);
        ps.flush();
        ps.println("FIN");
        ps.flush();
//        if (first) {
//            String lu = "";
//            while (true) {
//                String tmp = br.readLine();
//                if (tmp.compareTo("FIN") == 0)
//                    break;
//                lu = tmp;
//                System.out.println(lu);
//            }
//            this.init(lu);
//        }
        if( !first ){
            this.pong.add(new Racket(2, 250, 250));
            this.addPlayer();
            this.addNewClient(this.getWriter(position));
        }

    }



    public void aff() {
        for (int i = 0; i < pong.pongList.size(); i++) {
            System.out.println(pong.pongList.get(i).getPosition());
        }
    }

    public void connectionAccept(Socket socket) throws IOException {
        int pos = this.addReader(socket);
        InputStream is = this.getReader(pos).getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String lu = "";
        while (true) {
            String tmp = br.readLine();
            if (tmp.compareTo("FIN") == 0)
                break;
            lu = tmp;
            System.out.println(lu);
        }
        String[] message = lu.split(";");
        // si ce n'est pas un joueur on Quitte
        if (message.length != 3 && message[0].compareTo("Pong Play") != 0)
            return;
        String[] infoPort = message[1].split(" ");
        int port = Integer.parseInt(infoPort[1]);
        String[] connectionFirst = message[2].split(" ");
        String first = connectionFirst[1];

        if (first.compareTo("true") == 0){
            this.addWriter(new Socket(socket.getInetAddress(),port));
            System.out.println(socket.getInetAddress().getHostName());
            connectionServer(socket.getInetAddress().getHostName(),this.getWriter(pos).getLocalPort(),false);
        }
        else{
            String info="";
            while (true) {
                String tmp = br.readLine();
                if (tmp.compareTo("FIN") == 0)
                    break;
                info = tmp;
                System.out.println(lu);
            }
            this.init(info);
            System.out.println(this.nombrePlayer);
        }


    }


}