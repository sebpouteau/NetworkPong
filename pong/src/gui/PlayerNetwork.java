package src.gui;

import com.sun.org.apache.xpath.internal.SourceTree;
import src.util.VariableStatic;

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


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
    public void sendMessage(Socket socket,String message) throws IOException {
        OutputStream os = socket.getOutputStream();
        PrintStream ps = new PrintStream(os, false, "utf-8");
        ps.println(message);
        ps.flush();
//        ps.println(Protocol.MESSAGE_END);
//        ps.flush();
    }

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
     * Permet de lire les information recu durant la phase de connection d'un nouveau joueur
     * @param idSocket numero de la socket dans l'arrayList SocketPlayer
     * @return Retourne le String lu
     * @throws IOException
     */
    public String read(int idSocket) throws IOException{
        InputStream is = this.getSocket(idSocket).getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String lu = "";
        //while (true) {
            String tmp = br.readLine();
          //  if (tmp.compareTo(Protocol.MESSAGE_END) == 0)
            //    break;
            //lu = tmp;
        //}
        return tmp;
    }

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
     * @return retourne le String contenant la réponse du serveur
     * @throws IOException
     */
    public String[] connectionServer(String adress, int portConnection, boolean first) throws IOException {
        Socket s = connection(adress, portConnection);
        SocketPlayer socketPlayer = new SocketPlayer(s, portConnection);
        int position = this.addSocket(socketPlayer);
        /* envoie des informations de reconnaissance */
        String message = Protocol.identification(this.getPort(),first);
        sendMessage(this.getSocket(position),message);
        /* reception des informations envoyées par le serveur du premier joueur */
        String[] tabLecture= new String[2];
        tabLecture[0] = read(position);
        tabLecture[1] = read(position);

        System.out.println(tabLecture[0]);
        System.out.println(tabLecture[1]);

        return tabLecture;
    }

    /**
     * Fonction permettant d'accepter une connexion d'un joueur
     * @param socket Socket à accepter
     * @return return si Connection établie et joueur valide retourne EXIT_SUCCESS sinon retourne EXIT_FAILURE
     * @throws IOException
     */
    public int[] connectionAccept(Socket socket) throws IOException {
        SocketPlayer socketPlayer = new SocketPlayer(socket, 0);
        int position = this.addSocket(socketPlayer);
        String lu = read(position);
        System.out.println(lu);
        if (!Protocol.validPlayer(lu))
             position = -1;
        int port = Protocol.decryptPort(lu);
        this.getSocketPlayer(position).setPort(port);
        int t[] = new int[2];
        t[0] = position;
        System.out.println(Protocol.decryptFirst(lu));
        t[1] = Protocol.decryptFirst(lu)?1:0;
        System.out.println(t[1]);
        return t;
    }


}
