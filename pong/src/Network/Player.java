package src.Network;

import src.gui.*;

import java.io.*;
import java.net.Socket;

/**
 *  Classe contenant tout les informations d'un joueur
 *  ainsi que son serveur, et les connections avec les autres joueurs
 */
public class Player extends PlayerNetwork {
    private static int MAX_PLAYER = 4;
    private static int SCORE_FOR_BONUS = 5;
    private static int MYRACKET = 0;
    private Pong pong;
    private int idPlayer;
    private int nombrePlayer;
    private int maxPlayer;
    private boolean activateBonus = false;
    private int[] score;

    public Player(Pong pong) {
        super();
        this.score = new int[MAX_PLAYER];
        this.pong = pong;
        idPlayer = 1;
    }

/* =================================================
                      Getter and Setter
     ================================================= */

    public int getNombrePlayer() {
        return nombrePlayer;
    }

    public void setNombrePlayer(int nombrePlayer) {
        this.nombrePlayer = nombrePlayer;
    }

    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }

    public int getIdPlayer() {
        return this.idPlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public Pong getPong() {return pong;}

    public void addPlayer() {this.nombrePlayer++;}

    public int getScore(int i) {
        return score[i];
    }

    public void setScore(int pos,int score) {
        this.score[pos] = score;
    }

    public PongItem getMyRacket(){
        return getPong().getItem(MYRACKET);
    }

 /* =================================================
        Fonctions Lié au réseau Connection & Serveur
     ================================================= */

    /**
     * Permet d'initialisé les connexion d'un nouveau joueur par rapport à une liste reçu
     * @param message String contenant les information sur les connexion du jeu
     * @throws IOException
     * @throws InterruptedException
     */
    public void initialisationSocket(String message) throws IOException, InterruptedException {
        String[] socketList = message.split(";");
        for (String aSocketList : socketList) {
            String[] socket = aSocketList.split(" ");
            connectionServerInit(
                    Protocol.decryptAdress(socket),
                    Protocol.decryptPortSocket(socket),
                    false);
        }
    }

    /**
     * Fonction permettant de ce connecter à un serveur et d'initialisé le pong
     * @param adress addresse à se connecter
     * @param portConnection port de connection
     * @param first Vrai si première connection dans la partie Faux sinon
     * @throws IOException
     */
    public void connectionServerInit(String adress, int portConnection, boolean first) throws IOException, InterruptedException {
        int position = super.connectionServer(adress, portConnection, first);
        /* initialisation de tout les objets grâce à l'information reçu */
        if (first) {
            String tabSocket = read(position);
            String listItem = read(position);

            this.initialisationItem(listItem,getSocketPlayer(position));
            if (!tabSocket.isEmpty())
                this.initialisationSocket(tabSocket);
        }
        else{
            String myRacket = Protocol.informationItem(getPong().getItem(MYRACKET));
            Thread.sleep(1);
            sendMessage(getSocketPlayer(position), myRacket);
            String lu = read(position);
            String[] message = lu.split(" ");
            getSocketPlayer(position).setNumeroPlayer(Protocol.decryptId(message));
        }
    }

    /**
     * Fonction permettant d'accepter une connexion d'un joueur et
     * de l'ajouter dans ca liste Item, et lui envoyer la liste des item deja présent dans le jeu
     * @param socket Socket à accepter
     * @throws IOException
     */
    public void connectionAcceptPlayer(Socket socket) throws IOException, InterruptedException {
        boolean first = super.connectionAccept(socket);
        int pos = listSocketSize()-1;
        if (first)
            this.addNewClient(this.getSocketPlayer(pos),pos);
        else{
            String lu = read(pos);
            addRacketNewPlayer(lu,getSocketPlayer(pos));
            sendMessage(getSocketPlayer(pos), Protocol.informationItem(getMyRacket()));
            this.addPlayer();
        }
    }


    /* =================================================
     Fonctions traitement des joueur et item pendant le jeu
     ================================================= */

    /**
     * Permet d'initialiser un joueur en fonction d'un string reçu
     * @param message String contenant tout les objets du jeu
     * @param s SocketPlayer dont on a besoin pour recevoir les informations
     */
    public void initialisationItem(String message,SocketPlayer s) {
        String[] listItem = message.split(";");
        String[] item = listItem[MYRACKET].split(" ");
        this.setNombrePlayer(Protocol.decryptNumberPlayer(item));
        this.setIdPlayer(Protocol.decryptId(item));
        this.setMaxPlayer(Protocol.decryptMaxPlayer(item));
        s.setNumeroPlayer(Protocol.decryptIdPlayerConnected(item));
        for (String aListItem : listItem) {
            item = aListItem.split(" ");
            switch (Protocol.decryptClasseItem(item)) {
                case "Racket":
                    getPong().add(new Racket(Protocol.decryptId(item)));
                    break;
                case "Bonus":
                    getPong().add(new Bonus());
                    break;
                case "Ball":
                    getPong().add(new Ball(Protocol.decryptId(item)));
                    break;
            }
        }
        getPong().addKeyListener(getPong().getItem(MYRACKET));
    }

    /**
     * Ajoute la raquette d'un nouveau joueur qui c'est déjà connecté à un autre joueur avant lui
     * @param message String contenant les informations de la raqket du nouveau joueur
     */
    public void addRacketNewPlayer(String message, SocketPlayer s){
        String[] item = message.split(" ");
        Racket newRacket = new Racket(Protocol.decryptId(item));
        s.setNumeroPlayer(Protocol.decryptId(item));
        getPong().add(newRacket);
    }

    /**
     * Permet de lister et envoyer tout les éléments à un joueur
     * @param socket Socket à qui envoyer les information
     * @param position position de la socket dans la liste de socket
     * @throws IOException
     */
    public void addNewClient(SocketPlayer socket, int position) throws IOException, InterruptedException {
        /* envoie tout les objets présent dans le jeu */
        StringBuilder listOtherPlayer = new StringBuilder();
        for (int i = 0; i < position; i++) {
            listOtherPlayer.append(Protocol.infomationSocket(getSocketPlayer(i)));
        }
        /* envoie de la liste des connexions */
        sendMessage(socket, listOtherPlayer.toString());
        this.addPlayer();
        socket.setNumeroPlayer(this.nombrePlayer);
        /* creation nouvelle racket et envoie des items du jeu au nouveau joueur */
        Racket newRacket = new Racket(this.getNombrePlayer());
        String item = listItemGame(newRacket);
        this.getPong().add(newRacket);
        sendMessage(socket, item);
    }

    /**
     * suppromer un joueur qui n'est plus connecter grace à son numero de socket
     * @param idSocket numero de socket du joueur deconnecté
     * @throws IOException
     */
    public void removePlayer(int idSocket) throws IOException {
        for (int i= 0; i < getPong().listItemSize();i++) {
            if (getPong().getItem(i).getClass().getSimpleName().equals("Racket")
                    && getPong().getItem(i).getNumber() == getSocketPlayer(idSocket).getNumeroPlayer()) {
                getPong().removeItem(i);
                this.setNombrePlayer(this.getNombrePlayer() - 1);
                break;
            }
        }
        removeSocket(idSocket);

    }

    /**
     * Cette fonction permet de mettre à jour la ou les balle
     * ainsi que la raquette de celui qui à envoyer le message
     * @param idSocket numero de la socket dans l'arrayList SocketPlayer
     * @throws IOException
     */
    public void update(int idSocket) throws IOException {
        String message = null;
        try {
            message = read(idSocket);
        } catch (IOException e) {
            removePlayer(idSocket);
        }
        if (message!= null) {
            String[] item = message.split(";");
            for (String anItem : item) {
                String[] info = anItem.split(" ");
                if (Protocol.decryptClasseItem(info).equals("Racket"))
                    updateItem(info, "Racket");
                else if (Protocol.decryptClasseItem(info).equals("Ball") && idPlayerControlBall(info) != idPlayer)
                    updateItem(info, "Ball");
                else if (Protocol.decryptClasseItem(info).equals("Bonus")) {
                    for (int k = 0; k < getPong().listItemSize(); k++) {
                        if (getPong().getItem(k).getClass().getSimpleName().equals("Bonus")) {
                            getPong().getItem(k).setNumber(Protocol.decryptId(info));
                            ((Bonus)getPong().getItem(k)).setVisible(true);
                        }
                    }
                    updateItem(info, "Bonus");
                }
            }
        }
    }

    /**
     * Fonction permettant de mettre à jour un item grace à un message contenant ses information
     * @param message tring contenant les information d'un item normalisé selon le protocole
     * @param type    String permettant de chercher un type spécifique de la liste des item ex: "Raquet"
     * @return true si changement effectuer et valide, false si changement impossible car Triche
     */
    public boolean updateItem(String[] message, String type) {
        int idP = Protocol.decryptId(message);
        int x = Protocol.decryptX(message);
        int y = Protocol.decryptY(message);
        int speedX= Protocol.decryptSpeedX(message);
        int speedY= Protocol.decryptSpeedY(message);

        for (int k = 0; k < getPong().listItemSize(); k++) {
            if (getPong().getItem(k).getClass().getSimpleName().equals(type)
                    && getPong().getItem(k).getNumber() == idP) {
                if (getPong().getItem(k).notCheating(x,y,speedX,speedY)) {
                    getPong().getItem(k).setPosition(x, y);
                    getPong().getItem(k).setSpeed(speedX, speedY);
                }
                else{
                    System.out.println("tu triche");
                    break;
                }
            }
        }
        return true;
    }

    /* =================================================
                   Fonctions D'information
     ================================================= */

    /**
     * Calcul la somme de tout les score
     * @return la somme
     */
    public int sommeScore(){
        int somme=0;
        for (int i = 0; i < getNombrePlayer(); i++) {
            somme += getScore(i);
        }
        return somme;
    }

    /**
     * Met à jour les scores et permet d'activé le bonus
     */
    public void attributionScore() {
        for (int i = 0; i < getPong().listItemSize(); i++) {
            if (getPong().getItem(i) instanceof Ball) {
                Ball ball = (Ball) getPong().getItem(i);
                int playerLose = ball.getLosePlayerSize();
                if(playerLose != 0){
                    int somme = sommeScore();
                    for (int k = 0; k < getPong().listItemSize() ; k++) {
                        /* Si l'item est une racket et que le numero player lose est un joueur présent */
                        if (getPong().getItem(k) instanceof  Racket && getPong().getItem(k).getNumber() == playerLose ){
                            if (idPlayer ==playerLose && somme % (SCORE_FOR_BONUS * (getNombrePlayer()-1)) == 0 && sommeScore() != 0)
                                activateBonus = true;
                            for (int j = 0; j < getNombrePlayer(); j++) {
                                if (j != playerLose - 1)
                                    setScore(j, getScore(j) + 1);
                            }
                            ball.restart();
                        }
                    }
                }
            }
        }
    }

    /**
     * Permet de lister tout les items du jeu
     * @return String contenant tout les Items
     */
    private String listItemGame(Racket newRacket) {
        StringBuilder message = new StringBuilder();
        message.append(Protocol.attributionNewPlayer(this.getNombrePlayer(), this.getMaxPlayer(), newRacket, this.getIdPlayer()));
        message.append(";");
        for (int i = 0; i < getPong().listItemSize(); i++) {
            message.append(Protocol.informationItem(getPong().getItem(i)));
            message.append(";");
        }
        return message.toString();
    }

    /**
     * Permet de creer une chaine de caractère contenant les position de la raquette d'un joueur
     * ainsi que tout les positions des balles
     * @return String contenant les positions
     */
    public String information() {
        StringBuilder message = new StringBuilder();
        message.append(Protocol.informationItem(this.getPong().getItem(MYRACKET))).append(";");
        for (int i = 0; i < getPong().listItemSize(); i++) {
            if (getPong().getItem(i) instanceof Ball) {
                message.append(Protocol.informationItem(getPong().getItem(i))).append(";");
            }
            if (activateBonus && getPong().getItem(i) instanceof Bonus) {
                if (!((Bonus) getPong().getItem(i)).isActive() &&
                        !((Bonus) getPong().getItem(i)).isVisible()) {
                    ((Bonus) getPong().getItem(i)).bonusAleatoire();
                    message.append(Protocol.informationItem(getPong().getItem(i))).append(";");
                }
                activateBonus = false;
            }
        }
        return message.toString();
    }

    /**
     * Permet de connaitre l'id du joueur contolant la balle
     * @param message String contenant les information d'une balle normalisé selon le protocole
     * @return retourne le numero du joueur controlant la balle
     */
    public int idPlayerControlBall(String[] message) {
        int x = Protocol.decryptX(message);
        int y = Protocol.decryptY(message);
        int idPlayer = 0;
        int distanceMin = -1;
        for (int k = 0; k < getPong().listItemSize(); k++) {
            if (getPong().getItem(k) instanceof Racket) {
                int distance = (int) Math.sqrt(Math.pow((x - getPong().getItem(k).getPositionX()), 2) +
                        Math.pow((y - getPong().getItem(k).getPositionY()), 2));
                if (distance < distanceMin || distanceMin < 0) {
                    idPlayer = getPong().getItem(k).getNumber();
                    distanceMin = distance;
                }
            }
        }
        return idPlayer;
    }


}



