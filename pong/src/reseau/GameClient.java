package src.reseau;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
import src.gui.Pong;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by sebpouteau on 06/11/15.
 */
public class GameClient {
    private ArrayList<Socket> ArraySocketClient;
    private Pong pong;

    public GameClient(){
        ArraySocketClient = new ArrayList<>();
    }

    public void addArrayList(Socket s){
        this.ArraySocketClient.add(s);
    }
    private ArrayList<Socket> getArraySocket(){
        return ArraySocketClient;
    }

    private Socket getSocketClient(int indice){
        return ArraySocketClient.get(indice);
    }
    private String SendOtherPlayer(){
        StringBuffer message = new StringBuffer();
        for (int i = 0; i < this.getArraySocket().size(); i++) {
            message.append("IP " + this.getSocketClient(i).getInetAddress());
            message.append("PORT" + this.getSocketClient(i).getPort());
            message.append(";");
        }
        return message.toString();
    }

    private String SendAllItem(){
        StringBuffer message = new StringBuffer();
        for (int i = 0; i < this.getArraySocket().size(); i++) {
            message.append("IP " + this.getSocketClient(i).getInetAddress());
            message.append("IP " + this.getSocketClient(i).getPort());
            message.append(";");
        }
        return message.toString();
    }


    public void addNewClient(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
        PrintStream ps = new PrintStream(os,false,"utf-8");
        System.out.println("nouveau player");
        String lu;
        do{
            lu = br.readLine();
            System.out.println(lu);
        }while(lu.compareTo("FIN") != 0);
        System.out.println("fin lu");
        String[] message = lu.split(";");
        if (message.length != 2 && message[0].compareTo("Pong Play") != 0)
            return;
        //Ajout d'une racket a la liste
        System.out.println("fin lu");

        String[] first = message[1].split(":");
        if (first.length == 2 && first[1].compareTo("T") == 0) {
            ps.println(SendOtherPlayer());
            ps.flush();
            //ps.println(SendAllItem());
        }
        this.addArrayList(socket);

    }
    public static void main(String[] args) throws IOException {
        GameClient client = new GameClient();


        Pong pong = new Pong();
        if (args.length == 0) {
            ThreadSocketListen t = new ThreadSocketListen(client, Integer.parseInt(args[1]));
            t.start();
        }
        else{
            ThreadSocketListen t = new ThreadSocketListen(client, Integer.parseInt(args[2]));
            Socket player = new Socket(args[0],Integer.parseInt(args[1]));
            client.addArrayList(player);
            InputStream is = player.getInputStream();
            OutputStream os = player.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            PrintStream ps = new PrintStream(os,false,"utf-8");
            ps.println("Pong Play;first:T");
            ps.flush();
            ps.println("FIN");
            ps.flush();
            System.out.println("j'ai fini d'envoyer");
            while (true) {
                String lu = br.readLine();
                if (lu == null)
                    break;
                System.out.println(lu);
            }
        }

    }
}