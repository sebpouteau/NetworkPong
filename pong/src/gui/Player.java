package src.gui;

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
    public ArrayList<Socket> tabSocket;
    public int port;
    public int idplayer;
    public int nombrePlayer;

    public int addSocket(Socket socket) {
        this.tabSocket.add(socket);
        return this.tabSocket.size() - 1;
    }

    public Socket getSocket(int pos) {
        return this.tabSocket.get(pos);
    }


    public Player(Pong pong) {
        this.pong = pong;
        tabSocket = new ArrayList<Socket>();
    }

    /**
     * Permet de lsiter tout les items du jeu
     * @return String contenant tout les Items
     */
    private String listAllItem() {
        StringBuilder message = new StringBuilder();
        message.append(this.nombrePlayer + " " + this.nombrePlayer + " " +
                pong.getItem(pong.listItemSize() - 1).getPositionX() + " " +
                pong.getItem(pong.listItemSize() - 1).getPositionX());
        message.append(";");
        for (int i = 0; i < pong.listItemSize() - 1; i++) {
            if (pong.getItem(i) instanceof Ball) {
                message.append("BALL ").append(pong.getItem(i).getPositionX()).append(" ").append(pong.getItem(i).getPositionY());
            }
            if (pong.getItem(i) instanceof Racket) {
                message.append("RACKET " +
                        ((Racket) pong.getItem(i)).getIdPlayer() + " " +
                        pong.getItem(i).getPositionX() + " " +
                        pong.getItem(i).getPositionY());
            }
            message.append(";");
        }
        return message.toString();
    }

    /**
     * Envoie un message sur une socket
     * @param socket Socket
     * @param message message à envoyer
     * @throws IOException
     */
    public void sendMessage(Socket socket,String message) throws IOException {
        OutputStream os = socket.getOutputStream();
        PrintStream ps = new PrintStream(os, false, "utf-8");
        ps.println(message);
        ps.flush();
        ps.println("FIN");
        ps.flush();
    }

    /**
     * Permet d'initialiser un joueur en fonction d'un string reçu
     * @param message String contenant tout les objets du jeu
     */
    public void initialisation(String message) {
        String[] decoupe = message.split(";");
        String[] de = decoupe[0].split(" ");
        this.nombrePlayer = Integer.parseInt(de[0]);
        this.idplayer = Integer.parseInt(de[1]);
        ((Racket) (pong.getItem(0))).setIdPlayer(this.idplayer);
        pong.getItem(0).setPositionX(Integer.parseInt(de[2]));
        pong.getItem(0).setPositionY(Integer.parseInt(de[3]));

        for (int i = 1; i < decoupe.length - 1; i++) {
            de = decoupe[i].split(" ");
            if (de[0].compareTo("BALL") == 0) {
                pong.getItem(1).setPositionX(Integer.parseInt(de[1]));
                pong.getItem(1).setPositionY(Integer.parseInt(de[2]));
            }
            if (de[0].compareTo("RACKET") == 0) {
                pong.add(new Racket(Integer.parseInt(de[1]),
                        Integer.parseInt(de[2]), Integer.parseInt(de[3])));
            }

        }

    }

    public void addPlayer() {
        this.nombrePlayer++;
    }

    /**
     * Permet de lister et envoyer tout les éléments à un joueur
     * @param socket Socket à qui envoyer les information
     * @throws IOException
     */
    public void addNewClient(Socket socket) throws IOException {
        /* envoie tout les objets présent dans le jeu */
        String item = listAllItem();
        sendMessage(socket,item);
    }

    /**
     * Permet de creer une chaine de caractère contenant les position de la raquette d'un joueur
     * ainsi que tout les positions des balles
     * @return String contenant les positions
     */
    public String Information() {
        StringBuffer message = new StringBuffer();
        message.append("RACKET "
                + ((Racket) pong.getItem(0)).getIdPlayer() + " "
                + pong.getItem(0).getPositionX() + " "
                + pong.getItem(0).getPositionY()
                + ";");
        for (int i = 0; i < pong.listItemSize(); i++) {
            if (pong.getItem(i) instanceof Ball){
                message.append("BALL "

                        + pong.getItem(1).getPositionX() + " "
                        + pong.getItem(1).getPositionY());

            }
        }

        return message.toString();
    }

    public void update(int pos) throws IOException, ClassNotFoundException {
        InputStream is =this.getSocket(pos).getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String message = br.readLine();
        if (message != null) {
            System.out.println(message);
            String[] item = message.split(";");
            for (int i = 0; i < item.length; i++) {
                String[] info = item[i].split(" ");
                int j = 0;
                if (info[j].compareTo("BALL") == 0) {
                    if (Integer.parseInt(info[j + 1]) > 400 && this.idplayer == 1)
                        pong.getItem(1).setPosition(Integer.parseInt(info[j + 1]), Integer.parseInt(info[j + 2]));
                    if (Integer.parseInt(info[j + 1]) < 400 && this.idplayer == 2)
                        pong.getItem(1).setPosition(Integer.parseInt(info[j + 1]), Integer.parseInt(info[j + 2]));
                }
                if (info[j].compareTo("RACKET") == 0) {
                    int idP = Integer.parseInt(info[j + 1]);
                    for (int k = 0; k < pong.listItemSize(); k++) {
                        if (pong.getItem(k) instanceof Racket) {
                            if (((Racket) pong.getItem(k)).getIdPlayer() == idP) {
                                pong.getItem(k).setPosition(Integer.parseInt(info[j + 2]), Integer.parseInt(info[j + 3]));
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
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(adress, portConnection));
        /* permet d'ignorer Nagle */
        socket.socket().setTcpNoDelay(true);
        int position = this.addSocket(socket.socket());
        System.out.println(portConnection);
        /* envoie des informations de reconnaissance */
        String message  = "Pong Play;Port: " + this.port + ";ConnectionFirst " + first;
        sendMessage(this.getSocket(position),message);
        /* reception des informations envoyées par le serveur du premier joueur */
        String info =read(position);
        System.out.println(info);
        /* initialisation de tout les objets grâce à l'information reçu */
        this.initialisation(info);
    }


    public void aff() {
        for (int i = 0; i < pong.listItemSize(); i++) {
            System.out.println(pong.getItem(i).getPosition());
        }
    }

    public boolean validPlayer(String message) {
        String[] tabMessage = message.split(";");
        return tabMessage[0].compareTo("Pong Play") == 0 && tabMessage.length == 3;
    }

    public int decryptPort(String message) {
        String[] tabMessage = message.split(";");
        String[] infoPort = tabMessage[1].split(" ");
        return Integer.parseInt(infoPort[1]);

    }

    public String read(int pos) throws IOException, ClassNotFoundException {
        InputStream is = this.getSocket(pos).getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String lu = "";
        while (true) {
            String tmp = br.readLine();
            if (tmp.compareTo("FIN") == 0)
                break;
            lu = tmp;
        }
        return lu;
    }

    public void connectionAccept(Socket socket) throws IOException, ClassNotFoundException {
        int pos = this.addSocket(socket);
        String lu = read(pos);
        System.out.println(lu);
        if (!validPlayer(lu))
            return;
        System.out.println(lu);
        int port = decryptPort(lu);

        this.pong.add(new Racket(2, 785, 0));
        this.addPlayer();
        this.addNewClient(this.getSocket(pos));
        System.out.println(this.nombrePlayer);


    }


}