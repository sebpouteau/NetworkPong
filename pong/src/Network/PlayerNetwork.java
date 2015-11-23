package src.Network;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * PlayerNetwork
 */
public class PlayerNetwork {
    private ServerSocketChannel server;
    private ArrayList<SocketPlayer> tabSocket;
    private int port;

    public PlayerNetwork(){
        tabSocket = new ArrayList<>();
    }

    /* =================================================
                      Getter and Setter
     ================================================= */

    public ServerSocketChannel getServer(){
        return server;
    }

    public Socket getSocket(int pos) {
        return this.tabSocket.get(pos).getSocket();
    }

    public SocketPlayer getSocketPlayer(int pos) {
        return this.tabSocket.get(pos);
    }

    public int getPort() {
        return port;
    }

    public int listSocketSize(){
        return this.tabSocket.size();
    }


     /* =================================================
                         Fonctions
     ================================================= */

    /**
     * Initialise le serveur du joueur
     * @param port port d'écoute du serveur
     * @throws IOException
     */
    public void initServeur(int port) throws IOException {
        this.port = port;
        this.server = ServerSocketChannel.open();
        this.server.socket().bind(new InetSocketAddress(this.port));
        this.server.configureBlocking(false);
    }

    /**
     * Ajoute une Socket à la liste de socket existante
     * @param socket Socket à ajouter
     * @return retourne la position dans la liste de la socket
     */
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
    public void sendMessage(SocketPlayer socket,String message) throws IOException {
        socket.getPrintStream().println(message);
        socket.getPrintStream().flush();
    }

    /**
     * Permet de lire les information recu durant la phase de connection d'un nouveau joueur
     * @param idSocket numero de la socket dans l'arrayList SocketPlayer
     * @return Retourne le String lu
     * @throws IOException
     */
    public String read(int idSocket) throws IOException{
       return this.getSocketPlayer(idSocket).getBufferReader().readLine();
    }

    /**
     * Permet de ce connecter à un serveur
     * @param adress addresse à se connecter
     * @param portConnection port de connection
     * @return retourne la socket connecter
     * @throws IOException
     */
    public Socket connection(String adress, int portConnection) throws IOException {
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(adress, portConnection));
        /* permet d'ignorer Nagle */
        socket.socket().setTcpNoDelay(true);
        return socket.socket();
    }

    /**
     * Fonction permettant de ce connecter à un serveur
     * @param adress addresse à se connecter
     * @param portConnection port de connection
     * @param first Vrai si première connection dans la partie Faux sinon
     * @return la position de la nouvelle socket dans la liste des sockets
     * @throws IOException
     */
    public int connectionServer(String adress, int portConnection, boolean first) throws IOException {
        Socket s = connection(adress, portConnection);
        SocketPlayer socketPlayer = new SocketPlayer(s, portConnection);
        int position = this.addSocket(socketPlayer);
        /* envoie des informations de reconnaissance */
        String message = Protocol.identification(this.getPort(), first);
        sendMessage(this.getSocketPlayer(position),message);
        /* reception des informations envoyées par le serveur du premier joueur */
        return position;
    }

    /**
     * Fonction permettant d'accepter une connexion d'un joueur
     * @param socket Socket à accepter
     * @return true si premiere connection à un joueur, false sinon
     * @throws IOException
     */
    public boolean connectionAccept(Socket socket) throws IOException {
        SocketPlayer socketPlayer = new SocketPlayer(socket, 0);
        int position = this.addSocket(socketPlayer);
        String lu = read(position);
        if (!Protocol.validPlayer(lu))
             position = -1;
        int port = Protocol.decryptPort(lu);
        this.getSocketPlayer(position).setPort(port);
        return Protocol.decryptFirst(lu);
    }


}
