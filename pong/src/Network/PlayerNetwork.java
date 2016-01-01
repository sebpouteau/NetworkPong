package src.Network;

import java.io.*;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Enumeration;

public class PlayerNetwork {
    
    private ServerSocketChannel server;
    private ArrayList<SocketPlayer> tabSocket;
    private int port;

    public PlayerNetwork(){
        tabSocket = new ArrayList<SocketPlayer>();
    }

    /* ================================================
                      Getter and Setter
       ================================================ */

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

    /**
     * Recupere l'adresse du serveur.
     * @return l'adresse du serveur.
     * @throws IOException
     */
    public static String getAddressServeur() throws IOException {
        String ip="";
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;
                Enumeration<InetAddress> adress = iface.getInetAddresses();

                while(adress.hasMoreElements()) {
                    InetAddress addr = adress.nextElement();
                    ip = addr.getHostAddress();
                    if (ip.length() < 16){
                        if (!ip.startsWith("127") || ip.startsWith("0")){
                            return ip;
                        }
                    }
                }
            }
        return ip;
    }

    /**
     * Retourne la taille de la liste des sockets.
     * @return taille de la liste des sockets.
     */
    public int getListSocketSize(){
        return this.tabSocket.size();
    }

    /**
     * Supprime un socket player de la liste des sockets.
     * @param id numero de la socket à supprimer.
     * @throws IOException
     */
    public void removeSocket(int id) throws IOException {
        getSocket(id).close();
        tabSocket.remove(id);
    }

     /* ================================================
                         Fonctions
        ================================================ */

    /**
     * Initialise le serveur du joueur.
     * @param port port d'écoute du serveur.
     * @throws IOException
     */
    public void initServeur(int port) throws IOException {
        this.port = port;
        this.server = ServerSocketChannel.open();
        this.server.socket().bind(new InetSocketAddress(this.port));
        this.server.configureBlocking(false);
    }

    /**
     * Ajoute une Socket a la liste de sockets existante.
     * @param socket Socket a ajouter.
     * @return Retourne la position dans la liste de la socket.
     */
    public int addSocket(SocketPlayer socket) {
        this.tabSocket.add(socket);
        return this.tabSocket.size() - 1;
    }

    /**
     * Envoie un message sur une socket.
     * @param socket Socket.
     * @param message Message à envoyer.
     * @throws IOException
     */
    public void sendMessage(SocketPlayer socket,String message) throws IOException {
        socket.getPrintStream().println(message);
        socket.getPrintStream().flush();
    }

    /**
     * Lit les informations recues durant la phase de connection d'un nouveau joueur.
     * @param idSocket Numero de la socket dans l'ArrayList SocketPlayer.
     * @return Retourne le String lu.
     * @throws IOException
     */
    public String read(int idSocket) throws IOException {
        return this.getSocketPlayer(idSocket).getBufferReader().readLine();
    }

    /**
     * Permet de ce connecter à un serveur.
     * @param adress Addresse ou se connecter.
     * @param portConnection Port de connection.
     * @return Retourne la socket connectee.
     * @throws IOException
     */
    public Socket connection(String adress, int portConnection) throws IOException {
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(adress, portConnection));
        /* Permet d'ignorer Nagle. */
        socket.socket().setTcpNoDelay(true);
        return socket.socket();
    }

    /**
     * Fonction permettant de ce connecter a un serveur avec procedure d'identification.
     * @param adress Addresse ou se connecter.
     * @param portConnection Port de connection.
     * @param first Vrai si premiere connection dans la partie. Faux sinon.
     * @return La position de la nouvelle socket dans la liste des sockets.
     * @throws IOException
     */
    public int connectionServer(String adress, int portConnection, boolean first) throws IOException {
        Socket s = connection(adress, portConnection);
        SocketPlayer socketPlayer = new SocketPlayer(s, portConnection);
        int position = this.addSocket(socketPlayer);
        /* Envoie des informations de reconnaissance. */
        String message = Protocol.identification(this.getPort(), first);
        sendMessage(this.getSocketPlayer(position),message);
        /* Reception des informations envoyees par le serveur du premier joueur. */
        return position;
    }

    /**
     * Fonction permettant d'accepter la connexion d'un joueur.
     * @param socket Socket a accepter.
     * @return True si premiere connection à un joueur,False sinon.
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
