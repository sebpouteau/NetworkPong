import java.io.*;
import java.net.Socket;
import java.util.Iterator;

/**
 * Created by sebpouteau on 16/10/15.
 */
public class MonThreadChat extends Thread{
    Socket client;
    ServerChat server;
    public MonThreadChat (Socket s,ServerChat s1){
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
                    os = client1.getOutputStream();
                    ps = new PrintStream(os,false,"utf-8");
                    ps.println(lu);
                    ps.flush();
                }
            }
            client.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
