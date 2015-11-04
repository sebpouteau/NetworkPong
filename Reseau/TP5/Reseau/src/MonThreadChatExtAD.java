import java.io.*;
import java.net.Socket;
import java.util.Iterator;

/**
 * Created by sebpouteau on 16/10/15.
 */
public class MonThreadChatExtAD extends Thread{
    Socket client;
    ServerChatExt server;
    String message;
    public MonThreadChatExtAD(Socket s, ServerChatExt s1,int i){
        super();
        client = s;
        server = s1;
        if (i==0){
            message =part();
        }
        else{
            message = joinServeur();
        }
    }
    public String joinServeur(){
        return "join " + client.getInetAddress().toString();
    }
    public String part(){
        return "part" + client.getInetAddress().toString();

    }
    public void run(){
        try {
            OutputStream os;
            PrintStream ps;
            Iterator it =server.sendall();
            while (it.hasNext()) {
                Socket client1 = (Socket) it.next();
                if (client1.isClosed()) {
                    MonThreadChatExtAD t = new MonThreadChatExtAD(client1,server,0);
                    t.start();
                    server.removeClient(client1);

                }
                else {
                    os = client1.getOutputStream();
                    ps = new PrintStream(os, false, "utf-8");
                    ps.println(message);
                    ps.flush();
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
