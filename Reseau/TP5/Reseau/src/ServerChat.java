import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
/**
 * Created by sebpouteau on 16/10/15.
 */
public class ServerChat {
    Set<Socket> ensembleClient;
    public ServerChat()throws IOException{
        ensembleClient = new HashSet<Socket>();
        }

    public Iterator sendall(){
        return ensembleClient.iterator();
    }
    public static void main(String[] args) throws IOException {
        ServerChat schat = new ServerChat();
        ServerSocket socket = new ServerSocket(7777);

        while (true) {
            Socket client = socket.accept();
            synchronized (schat.ensembleClient) {
               schat.ensembleClient.add(client);
            }
            MonThreadChat th = new MonThreadChat(client,schat);
            th.start();

        }

    }
}
