package src.reseau;

import src.gui.Pong;
import src.gui.Window;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
/**
 * Created by ysaemery on 04/11/15.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 7777);
        OutputStream os = s.getOutputStream();
        InputStream is = s.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
        PrintStream ps = new PrintStream(os,false,"utf-8");
        ps.println("bonjour");
        ps.flush();
        //System.out.println(is.read());
        Pong pong = new Pong();



        Window window = new Window(pong);
        window.displayOnscreen();
        s.close();

    }
}