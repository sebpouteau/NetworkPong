package src.reseau;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

/**
 * Created by ysaemery on 04/11/15.
 */
public class ThreadSocketListen extends Thread {
    ServerSocket socket;

    GameClient client;
    public ThreadSocketListen(GameClient client, int port) {
        super();
        this.client = client;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            while (true) {

                Socket socket2 = socket.accept();

                client.addNewClient(socket2);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

