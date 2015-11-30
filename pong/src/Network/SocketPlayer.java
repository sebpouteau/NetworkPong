package src.Network;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe contenant la socket d'un joueur
 * ainsi que des informations supplémentaire sur le joueur
 */
public class SocketPlayer {
    private Socket socket;
    private int port;
    private String adress;
    private BufferedReader bufferReader;
    private PrintStream printStream;
    private int numeroPlayer;


    public SocketPlayer(Socket socket, int port) throws IOException {
        this.socket=socket;
        InputStream inputStream= this.getSocket().getInputStream();
        this.bufferReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        OutputStream outputStream = getSocket().getOutputStream();
        this.printStream = new PrintStream(outputStream, false, "utf-8");
        this.port = port;
        this.adress = socket.getInetAddress().getHostAddress();
    }

    public int getNumeroPlayer() {
        return numeroPlayer;
    }

    public void setNumeroPlayer(int numeroPlayer) {
        this.numeroPlayer = numeroPlayer;
    }

    public BufferedReader getBufferReader() {
        return bufferReader;
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAdress() {
        return adress;
    }



}
