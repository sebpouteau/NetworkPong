package src.Network;

import src.gui.*;
import java.io.*;
import java.net.Socket;

public class Player extends PlayerNetwork {

    private static int SCORE_FOR_BONUS = 5;
    private static int MYRACKET = 0;
    private Pong pong;
    private int idplayer;
    private int NumberPlayer;
    private int maxPlayer;
    private boolean activateBonus = false;

    public Player(Pong pong) {
        super();
        this.pong = pong;
        idplayer = 1;
    }

    /* =================================================
                      Getter and Setter
       ================================================= */

    public int getNumberPlayer() {
        return NumberPlayer;
    }

    public void setNumberPlayer(int NumberPlayer) {
        this.NumberPlayer = NumberPlayer;
    }

    public void setIdplayer(int idplayer) {
        this.idplayer = idplayer;
    }

    public int getIdplayer() {
        return this.idplayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public Pong getPong() {return pong;}

    public PongItem getMyRacket(){
        return getPong().getItem(MYRACKET);
    }

    public void addPlayer() {this.NumberPlayer++;}

    /**
     * Calcule la somme des scores du jeu.
     * @return Somme des scores.
     */
    public int sommeScore(){
        int somme=0;
        for (int i = 1; i < getNumberPlayer()+1; i++) {
            somme += this.getPong().getScore(i);
        }
        return somme;
    }

    /**
     * Fonction permettant d'attribuer les scores à chaque joueur.
     */
    public void attributionScore() {
        for (int i = 0; i < getPong().listItemSize(); i++) {
            if (getPong().getItem(i) instanceof Ball) {
                Ball ball = (Ball) getPong().getItem(i);
                int playerLose = ball.losePlayerSize();
                if(playerLose != 0){
                    int somme = sommeScore();
                    for (int k = 0; k < getPong().listItemSize() ; k++) {
                        /* Si l'item est une raquette et que le numero du playerLose est bien un joueur alors: */
                        if (getPong().getItem(k) instanceof  Racket && getPong().getItem(k).getNumber() == playerLose ){
                            if (idplayer==playerLose)
                                pong.setIfStart(true);

                            if (idplayer==playerLose && somme % (SCORE_FOR_BONUS * (getNumberPlayer()-1)) == 0 && sommeScore() != 0)
                                activateBonus = true;
                            for (int j = 0; j < getPong().listItemSize(); j++) {
                                if(getPong().getItem(j) instanceof Racket){
                                    int numberPlayer = getPong().getItem(j).getNumber();
                                    if (numberPlayer != playerLose) {
                                        this.getPong().setScore(numberPlayer , this.getPong().getScore(numberPlayer) + 1);
                                    }
                                }
                            }
                            Pong.setWaitPlayer(ball.restart(getPong().getItem(k)));
                        }
                }
                }
            }
        }
    }

     /* ================================================
                         Functions
        ================================================ */

    /**
     * Liste tous les items du jeu.
     * @return String contenant tous les Items.
     */
    private String listItemGame(Racket newRacket) {
        StringBuilder message = new StringBuilder();
        message.append(Protocol.attributionNewPlayer(this.getNumberPlayer(), this.getMaxPlayer(), newRacket, this.getIdplayer()));
        message.append(";");
        for (int i = 0; i < getPong().listItemSize(); i++) {
            message.append(Protocol.informationItem(getPong().getItem(i)));
            message.append(";");
        }
        return message.toString();
    }

    /**
     * Cree une chaine de caractere contenant les positions de la raquette d'un joueur
     * ainsi que toutes les positions des balles.
     * @return String contenant les positions.
     */
    public String information() {
        StringBuilder message = new StringBuilder();
        message.append(Protocol.idPlayer(idplayer));
        message.append(Protocol.informationItem(this.getPong().getItem(MYRACKET))).append(";");
        for (int i = 0; i < getPong().listItemSize(); i++) {
            if (getPong().getItem(i) instanceof Ball ) {
                message.append(Protocol.informationItem(getPong().getItem(i))).append(";");
            }
            if (activateBonus && pong.getIfStart()==false){
                if (getPong().getItem(i) instanceof Bonus){
                    if (! ((Bonus) getPong().getItem(i)).isActive() &&
                            !((Bonus)getPong().getItem(i)).isVisible()) {
                        ((Bonus) getPong().getItem(i)).bonusAleatoire();
                        message.append(Protocol.informationItem(getPong().getItem(i))).append(";");
                    }
                    activateBonus = false;
                }
            }
        }
        message.append(Protocol.informationScore(this));
        return message.toString();
    }

    /**
     * Permet de connaitre l'id du joueur controlant la balle.
     * @param message String contenant les information d'une balle normalisé selon le protocole.
     * @return Retourne le numero du joueur controlant la balle.
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

    /**
     * Initialise un joueur en fonction d'un string reçu.
     * @param message String contenant tous les objets du jeu.
     * @param s SocketPlayer dont on a besoin pour recevoir les informations.
     */
    public void initializationItem(String message,SocketPlayer s) {
        String[] listItem = message.split(";");
        String[] item = listItem[MYRACKET].split(" ");
        this.setNumberPlayer(Protocol.decryptNumberPlayer(item));
        this.setIdplayer(Protocol.decryptId(item));
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
                default:
                    getPong().add(new Ball(Protocol.decryptId(item)));
                    break;
            }
        }
        getPong().addKeyListener((Racket)getPong().getItem(0));
    }

    /**
     * Initialise les connexions d'un nouveau joueur par rapport à une liste reçu
     * @param message String contenant les informations sur les connexions du jeu.
     * @throws IOException
     * @throws InterruptedException
     */
    public void initializationSocket(String message) throws IOException, InterruptedException {
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
     * Ajoute la raquette d'un nouveau joueur deja connecte a un autre joueur avant lui.
     * @param message String contenant les informations de la raquette du nouveau joueur.
     */
    public void addRacketNewPlayer(String message, SocketPlayer s){
        String[] item = message.split(" ");
        Racket newRacket = new Racket(Protocol.decryptId(item));
        s.setNumeroPlayer(Protocol.decryptId(item));
        getPong().add(newRacket);
    }

    /**
     * Liste et envoie tous les elements à un joueur.
     * @param socket Socket à qui envoyer les informations.
     * @param position position de la socket dans la liste de socket.
     * @throws IOException
     */
    public void addNewClient(SocketPlayer socket, int position) throws IOException, InterruptedException {
        /* Envoie de tous les objets present dans le jeu. */
        StringBuilder listOtherPlayer = new StringBuilder();
        for (int i = 0; i < position; i++) {
            listOtherPlayer.append(Protocol.infomationSocket(getSocketPlayer(i)));
        }
        /* Envoi de la liste des connexions. */
        sendMessage(socket, listOtherPlayer.toString());
        this.addPlayer();
        socket.setNumeroPlayer(this.NumberPlayer);
        /* Creation d'une nouvelle raquette et envoi des items du jeu au nouveau joueur. */
        Racket newRacket = new Racket(this.getNumberPlayer());
        String item = listItemGame(newRacket);
        this.getPong().add(newRacket);
        Thread.sleep(1);
        sendMessage(socket, item);
    }

    /**
     * Supprimer un joueur lorsqu'il est deconnecte.
     * @param idSocket numero de la socket du joueur a supprimer.
     * @throws IOException
     */
    public void removePlayer(int idSocket) throws IOException {
        for (int i= 0; i < getPong().listItemSize();i++) {
            if (getPong().getItem(i).getClass().getSimpleName().equals("Racket")
                    && getPong().getItem(i).getNumber() == getSocketPlayer(idSocket).getNumeroPlayer()) {
                getPong().removeItem(i);
                this.setNumberPlayer(this.getNumberPlayer() - 1);
                break;
            }
        }
        removeSocket(idSocket);
    }

    /**
     * Cette fonction permet de mettre a jour la ou les balles
     * ainsi que la raquette de celui qui a envoye le message.
     * @param idSocket numero de la socket dans l'ArrayList SocketPlayer.
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
            String[] autentification = item[0].split(" ");
            int idPlayerSent = Protocol.decryptId(autentification);
            for (int i =1; i< item.length;i++) {
                String[] info = item[i].split(" ");
                if (Protocol.decryptClasseItem(info).equals("Racket"))
                    updateItem(info, "Racket");
                else if (Protocol.decryptClasseItem(info).equals("Ball") && idPlayerControlBall(info) == idPlayerSent) {
                    updateItem(info, "Ball");
                }
                else if (Protocol.decryptClasseItem(info).equals("Bonus")) {
                        for (int k = 0; k < getPong().listItemSize(); k++) {
                            if (getPong().getItem(k).getClass().getSimpleName().equals("Bonus")) {
                                getPong().getItem(k).setNumber(Protocol.decryptId(info));
                                ((Bonus)getPong().getItem(k)).setVisible(true);
                            }
                        }
                    updateItem(info, "Bonus");
                }
                else if (Protocol.decryptClasseItem(info).equals("Score")){
                    int id = Protocol.decryptId(info);
                    int newScore = Protocol.decryptScorePlayer(info);
                    if (newScore > getPong().getScore(id) + 1){
                        System.out.println(id + " triche");
                    }
                }
            }
        }
    }

    /**
     * Met a jour un item grace a un message contenant ses informations.
     * @param message String contenant les informations d'un item normalise selon le protocole.
     * @param type    String permettant de chercher un type specifique de la liste des items ex: "Racket".
     */
    public void updateItem(String[] message, String type) {
        int idP = Protocol.decryptId(message);
        int x = Protocol.decryptX(message);
        int y = Protocol.decryptY(message);
        int speedX = Protocol.decryptSpeedX(message);
        int speedY = Protocol.decryptSpeedY(message);
        for (int k = 0; k < getPong().listItemSize(); k++) {
            if (getPong().getItem(k).getClass().getSimpleName().equals(type)
                    && getPong().getItem(k).getNumber() == idP) {
                    getPong().getItem(k).setPosition(x, y);
                    getPong().getItem(k).setSpeed(speedX, speedY);
                if (!getPong().getItem(k).notCheating(x,y, speedX,speedY))
                    System.out.println(idP + "triche ");
            }
        }
    }

    /**
     * Fonction permettant de ce connecter a un serveur et d'initialiser le pong.
     * @param adress addresse ou se connecter.
     * @param portConnection port de connection.
     * @param first Vrai si c'est la premiere connection dans la partie Faux sinon.
     * @throws IOException
     */
    public void connectionServerInit(String adress, int portConnection, boolean first) throws IOException, InterruptedException {
        int position = super.connectionServer(adress, portConnection, first);
        /* initialisation de tous les objets grace a l'information reçu */
        if (first) {
            String tabSocket = read(position);
            String listItem = read(position);

            this.initializationItem(listItem, getSocketPlayer(position));
            if (!tabSocket.isEmpty())
                this.initializationSocket(tabSocket);
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
     * Accepte la connexion d'un joueur, l'ajoute dans sa liste Item,
     * et lui envoie la liste des items deja presents dans le jeu.
     * @param socket Socket a accepter.
     * @throws IOException
     */
    public void connectionAcceptPlayer(Socket socket) throws IOException, InterruptedException {
        boolean first = super.connectionAccept(socket);
        int pos = getListSocketSize()-1;
        if (first)
            this.addNewClient(this.getSocketPlayer(pos),pos);
        else{
            String lu = read(pos);
            addRacketNewPlayer(lu,getSocketPlayer(pos));
            sendMessage(getSocketPlayer(pos), Protocol.informationItem(getMyRacket()));
            this.addPlayer();
        }
    }

}



