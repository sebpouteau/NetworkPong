import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sebpouteau on 16/10/15.
 */
public class ServerChatExt {
    Set<Socket> ensembleClient;
    public ServerChatExt()throws IOException{
        ensembleClient = new HashSet<Socket>();
        }

    public Iterator sendall(){
        return ensembleClient.iterator();
    }

    public void removeClient(Socket c){
        ensembleClient.remove(c);
    }


    public static void main(String[] args) throws IOException {
        ServerChatExt schat = new ServerChatExt();
        ServerSocket socket = new ServerSocket(7777);

        while (true) {
            Socket client = socket.accept();
            synchronized (schat.ensembleClient) {
                schat.ensembleClient.add(client);
                MonThreadChatExtAD t= new MonThreadChatExtAD(client,schat,1);
                t.start();
            }
            MonThreadChatExt th = new MonThreadChatExt(client,schat);
            th.start();

        }

    }
}
