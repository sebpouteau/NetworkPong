package src.gui;

import java.io.*;
import java.net.Socket;

/**
 * Created by seb on 11/11/2015.
 */
public class Player extends PlayerNetwork{

    private static int MYRACKET= 0;
    public Pong pong;

    public int idplayer;
    public int nombrePlayer;

    public Player(Pong pong) {
        super();
        this.pong = pong;
        idplayer = 1;
    }

    /**
     * Permet de lister tout les items du jeu
     * @return String contenant tout les Items
     */
    private String listAllItem() {
        StringBuilder message = new StringBuilder();
        message.append(this.nombrePlayer + " " + this.nombrePlayer + " " +
                pong.getItem(pong.listItemSize() - 1).getPositionX() + " " +
                pong.getItem(pong.listItemSize() - 1).getPositionX());
        message.append(";");
        for (int i = 0; i < pong.listItemSize() - 1; i++) {
            if (pong.getItem(i) instanceof Ball) {
                message.append("BALL ").append(pong.getItem(i).getPositionX()).append(" ").append(pong.getItem(i).getPositionY());
            }
            if (pong.getItem(i) instanceof Racket) {
                message.append("RACKET " +
                        pong.getItem(i).getNumber() + " " +
                        pong.getItem(i).getPositionX() + " " +
                        pong.getItem(i).getPositionY());
            }
            message.append(";");
        }
        return message.toString();
    }



    /**
     * Permet d'initialiser un joueur en fonction d'un string reçu
     * @param message String contenant tout les objets du jeu
     */
    public void initialisation(String message) {
        String[] decoupe = message.split(";");
        String[] de = decoupe[0].split(" ");
        this.nombrePlayer = Integer.parseInt(de[0]);
        this.idplayer = Integer.parseInt(de[1]);
        pong.getItem(MYRACKET).setNumber(this.idplayer);
        pong.getItem(MYRACKET).setPositionX(Integer.parseInt(de[2]));
        pong.getItem(MYRACKET).setPositionY(Integer.parseInt(de[3]));
        for (int i = 1; i < decoupe.length - 1; i++) {
            de = decoupe[i].split(" ");
            if (de[0].compareTo("BALL") == 0) {
                pong.getItem(1).setPositionX(Integer.parseInt(de[1]));
                pong.getItem(1).setPositionY(Integer.parseInt(de[2]));
            }
            if (de[0].compareTo("RACKET") == 0) {
                pong.add(new Racket(Integer.parseInt(de[1]),
                        Integer.parseInt(de[2]), Integer.parseInt(de[3])));
            }

        }

    }

    public void addPlayer() {
        this.nombrePlayer++;
    }

    /**
     * Permet de lister et envoyer tout les éléments à un joueur
     * @param socket Socket à qui envoyer les information
     * @throws IOException
     */
    public void addNewClient(Socket socket) throws IOException {
        /* envoie tout les objets présent dans le jeu */
        String item = listAllItem();
        sendMessage(socket,item);
    }

    /**
     * Permet de creer une chaine de caractère contenant les position de la raquette d'un joueur
     * ainsi que tout les positions des balles
     * @return String contenant les positions
     */
    public String Information() {
        StringBuffer message = new StringBuffer();
        message.append("Racket "
                + pong.getItem(MYRACKET).getNumber() + " "
                + pong.getItem(MYRACKET).getPositionX() + " "
                + pong.getItem(MYRACKET).getPositionY() + " "
                + pong.getItem(MYRACKET).getSpeedX() + " "
                + pong.getItem(MYRACKET).getSpeedY() + " "

                + ";");
        for (int i = 0; i < pong.listItemSize(); i++) {
            if (pong.getItem(i) instanceof Ball){
                message.append("Ball "+
                        pong.getItem(i).getNumber() + " "+
                        + pong.getItem(i).getPositionX() + " "
                        + pong.getItem(i).getPositionY() + " "
                        + pong.getItem(i).getSpeedX() + " "
                        + pong.getItem(i).getSpeedY() + " "
                        +";");

            }
        }
        return message.toString();
    }

    public void update(int pos) throws IOException, ClassNotFoundException {
        InputStream is = this.getSocket(pos).getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String message = br.readLine();
        if (message != null) {
            String[] item = message.split(";");
            for (int i = 0; i < item.length; i++) {
                String[] info = item[i].split(" ");
                int j = 0;
                //if (info[0].equals("Ball")) {
                    if (verificationSetBall(info) != idplayer)
                        updateItem(info, "Ball");
                //}

                updateItem(info, "Racket");
            }
        }
    }


    public int verificationSetBall(String[] message){
        int x = Integer.parseInt(message[2]);
        int y = Integer.parseInt(message[3]);
        int idPlayer = 0;
        int distanceMin = 100000000;
        for (int k = 0; k < pong.listItemSize(); k++) {
            if (pong.getItem(k) instanceof Racket) {
                int distance = (int)Math.sqrt( Math.pow((x-pong.getItem(k).getPositionX()),2) +
                        Math.pow((y-pong.getItem(k).getPositionY()),2));
                if (distance < distanceMin) {
                    idPlayer = pong.getItem(k).getNumber();
                    distanceMin=distance;
                }
            }
        }
        return idPlayer;
    }


    public void updateItem (String[] message, String type){
        if (!message[0].equals(type))
            return;
        int idP = Integer.parseInt(message[1]);
        int x = Integer.parseInt(message[2]);
        int y = Integer.parseInt(message[3]);
        int speedX = Integer.parseInt(message[4]);
        int speedY = Integer.parseInt(message[5]);
        for (int k = 0; k < pong.listItemSize(); k++) {
            if (pong.getItem(k).getClass().getSimpleName().equals(type)) {
                if (pong.getItem(k).getNumber() == idP) {
                    pong.getItem(k).setPosition(x,y);
                    if (type.equals("Ball"))
                        pong.getItem(k).setSpeed(speedX,speedY);
                    break;
                }
            }
        }
    }







    public void connectionServerInit(String adress, int portConnection, boolean first) throws IOException, ClassNotFoundException {
        String info = super.connectionServer(adress,portConnection,first);
        this.initialisation(info);
    }


    public int connectionAccept(Socket socket) throws IOException, ClassNotFoundException {
        int position = super.connectionAccept(socket);
        this.pong.add(new Racket(2, 785, 0));
        this.addPlayer();
        this.addNewClient(this.getSocket(position));
        return 0;
    }


}