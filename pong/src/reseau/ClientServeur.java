package src.reseau;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

/**
 * Created by ysaemery on 04/11/15.
 */
public class ClientServeur extends Thread {
    ServerSocket socket;
    public ClientServeur(){
        super();

        try {
            socket = new ServerSocket(7777);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void run(){
        try {
            while (true) {
                Socket socket2 = socket.accept();
                OutputStream os = socket2.getOutputStream();
                InputStream is = socket2.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
                PrintStream ps = new PrintStream(os,false,"utf-8");
                while (true) {
                    String lu = br.readLine();
                    if (lu == null)
                        break;
                    System.out.println(lu);

                }
                socket2.close();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

