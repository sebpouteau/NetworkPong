import java.io.*;
import java.net.Socket;
import java.util.Iterator;

/**
 * Created by sebpouteau on 16/10/15.
 */
public class MonThreadChatExt extends Thread{
    Socket client;
    ServerChatExt server;
    public MonThreadChatExt(Socket s, ServerChatExt s1){
        super();
        client = s;
        server = s1;
    }

    public void run(){
        try {
            OutputStream os;
            InputStream is = client.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            PrintStream ps;
            while (true) {
                String lu = br.readLine();
                if (lu == null)
                    break;
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
                        ps.println(lu);
                        ps.flush();
                    }
                }
            }
            client.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
