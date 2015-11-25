package src.Network;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by sebpouteau on 17/11/15.
 */
public class SocketPlayer {
    private Socket socket;
    private int port;
    private String address;
    private InputStream inputStream;
    private BufferedReader bufferReader;
    private OutputStream outputStream;// = socket.getOutputStream();
    private PrintStream printStream;// = new PrintStream(os, false, "utf-8");
    private int numeroPlayer;


    public SocketPlayer(Socket socket, int port) throws IOException {
        this.socket=socket;
        this.inputStream= this.getSocket().getInputStream();
        this.bufferReader = new BufferedReader(new InputStreamReader(this.getInputStream(), "utf-8"));
        this.outputStream = getSocket().getOutputStream();
        this.printStream = new PrintStream(this.getOutputStream(), false, "utf-8");
        this.port = port;
        this.address = socket.getInetAddress().getHostAddress();
    }

    public int getNumeroPlayer() {
        return numeroPlayer;
    }

    public void setNumeroPlayer(int numeroPlayer) {
        this.numeroPlayer = numeroPlayer;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public BufferedReader getBufferReader() {
        return bufferReader;
    }

    public OutputStream getOutputStream() {
        return outputStream;
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
        return address;
    }



}
