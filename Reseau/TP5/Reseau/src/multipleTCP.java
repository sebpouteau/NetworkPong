/**
 * Created by sebpouteau on 16/10/15.
 */
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
public class multipleTCP {
    public static void main(String[] args) throws IOException {

        ServerSocket socket = new ServerSocket(7777);
        while (true) {
            Socket socket2 = socket.accept();
            MonThread th = new MonThread(socket2);
            th.start();

        }


    }
}
