import java.io.*;
import java.net.Socket;

/**
 * Created by sebpouteau on 16/10/15.
 */
public class MonThread extends Thread {
    private Socket client;

public MonThread (Socket s){
        super();
        client = s;
    }

    public void run(){
        try {
            OutputStream os = client.getOutputStream();
            InputStream is = client.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            PrintStream ps = new PrintStream(os,false,"utf-8");
            while (true) {
                String lu = br.readLine();
                if (lu == null)
                    break;
                ps.println(lu);
                ps.flush();
            }
            client.close();
        }
         catch (IOException e) {
            e.printStackTrace();
        }
    }
}
