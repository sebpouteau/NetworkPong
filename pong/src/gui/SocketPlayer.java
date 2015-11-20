package src.gui;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by sebpouteau on 17/11/15.
 */
public class SocketPlayer {
    private Socket socket;
    private int port;
    private String address;

    public SocketPlayer(Socket socket, int port){
        this.socket=socket;
        this.port = port;
        this.address = socket.getInetAddress().getHostAddress();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
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

    public void setAddress(String address) {
        this.address = address;
    }


}
