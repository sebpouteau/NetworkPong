package src.gui;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Created by sebpouteau on 18/11/15.
 */
public class PlayerNetwork {
    private ServerSocketChannel server;
    private ArrayList<SocketPlayer> tabSocket;
    private int port;

    public ServerSocketChannel getServer(){
        return server;
    }

    public int listSocketSize(){
        return this.tabSocket.size();
    }
    public PlayerNetwork(){
        tabSocket = new ArrayList<SocketPlayer>();
    }

    public Socket getSocket(int pos) {
        return this.tabSocket.get(pos).getSocket();
    }
    public SocketPlayer getSocketPlayer(int pos) {
        return this.tabSocket.get(pos);
    }

    public int addSocket(SocketPlayer socket) {
        this.tabSocket.add(socket);
        return this.tabSocket.size() - 1;
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

    public void initServeur(int port) throws IOException {
        this.port = port;
        this.server = ServerSocketChannel.open();
        this.server.socket().bind(new InetSocketAddress(this.port));
        this.server.configureBlocking(false);
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
    public boolean validPlayer(String message) {
        String[] tabMessage = message.split(";");
        return tabMessage[0].compareTo("Pong Play") == 0 && tabMessage.length == 3;
    }

    public int decryptPort(String message) {
        String[] tabMessage = message.split(";");
        String[] infoPort = tabMessage[1].split(" ");
        return Integer.parseInt(infoPort[1]);

    }

    public String connectionServer(String adress, int portConnection, boolean first) throws IOException, ClassNotFoundException {
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(adress, portConnection));
        /* permet d'ignorer Nagle */
        socket.socket().setTcpNoDelay(true);
        SocketPlayer socketPlayer = new SocketPlayer(socket.socket(), portConnection);
        int position = this.addSocket(socketPlayer);
        /* envoie des informations de reconnaissance */
        String message  = "Pong Play;Port: " + this.port + ";ConnectionFirst " + first;
        sendMessage(this.getSocket(position),message);
        /* reception des informations envoyées par le serveur du premier joueur */
        return read(position);
        /* initialisation de tout les objets grâce à l'information reçu */
        //this.initialisation(info);
    }
    public int connectionAccept(Socket socket) throws IOException, ClassNotFoundException {
        SocketPlayer socketPlayer = new SocketPlayer(socket, 0);
        int position = this.addSocket(socketPlayer);
        String lu = read(position);
        if (!validPlayer(lu))
            return 0;
        int port = decryptPort(lu);
        this.getSocketPlayer(position).setPort(port);
        System.out.println(socketPlayer.getPort() + " " + socketPlayer.getAddress());
        return position;
    }
}
