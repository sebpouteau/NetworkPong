package src.Network;

import src.gui.*;
import java.io.*;
import java.net.Socket;

/**
 *  Player
 */
public class Player extends PlayerNetwork {

    private static int MYRACKET = 0;
    private Pong pong;
    private int idplayer;
    private int nombrePlayer;
    private int maxPlayer;

    public Player(Pong pong) {
        super();
        this.pong = pong;
        idplayer = 1;
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

    public void setIdplayer(int idplayer) {
        this.idplayer = idplayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }
    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public Pong getPong() {return pong;}

    public void addPlayer() {this.nombrePlayer++;}

    public void getScore(){
        for (int i = 0; i < getPong().listItemSize() ; i++) {
            if(getPong().getItem(i) instanceof Ball){
                Ball b =(Ball) getPong().getItem(i);
                int player = b.getLosePlayerSize();
                if(player != 0 && player <= nombrePlayer ){
                    //tous les autres gagne un point!
                    b.restart();
                }
            }
        }
    }

    public PongItem getMyRacket(){
        return getPong().getItem(MYRACKET);
    }


     /* =================================================
                         Fonctions
     ================================================= */

    /**
     * Permet de lister tout les items du jeu
     *
     * @return String contenant tout les Items
     */
    private String listItemGame(Racket newRacket) {
        StringBuilder message = new StringBuilder();
        message.append(Protocol.attributionNewPlayer(this.getNombrePlayer(), this.getMaxPlayer(), newRacket));
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
     *
     * @return String contenant les positions
     */
    public String information() {
        StringBuilder message = new StringBuilder();
        message.append(Protocol.informationItem(this.getPong().getItem(MYRACKET))).append(";");
        for (int i = 0; i < getPong().listItemSize(); i++) {
            if (getPong().getItem(i) instanceof Ball) {
                message.append(Protocol.informationItem(getPong().getItem(i))).append(";");
            }
        }
        return message.toString();
    }

    /**
     * Permet de connaitre l'id du joueur contolant la balle
     *
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

    /**
     * Permet d'initialiser un joueur en fonction d'un string reçu
     *
     * @param message String contenant tout les objets du jeu
     */
    public void initialisationItem(String message) {
        String[] listItem = message.split(";");
        System.out.println(listItem[0]);
        String[] item = listItem[MYRACKET].split(" ");
        this.setNombrePlayer(Protocol.decryptNumberPlayer(item));
        this.setIdplayer(Protocol.decryptId(item));
        this.setMaxPlayer(Protocol.decryptMaxPlayer(item));
        for (String aListItem : listItem) {
            item = aListItem.split(" ");
            if (Protocol.decryptClasseItem(item).compareTo("Racket") == 0)
                getPong().add(new Racket(Protocol.decryptId(item),
                        Protocol.decryptX(item),
                        Protocol.decryptY(item)));
            else
                getPong().add(new Ball(Protocol.decryptId(item),
                        Protocol.decryptX(item),
                        Protocol.decryptY(item)));
        }
        getPong().addKeyListener(getPong().getItem(0));


    }

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

    /**e
     * Ajoute la raquette d'un nouveau joueur qui c'est déjà connecté à un autre joueur avant lui
     * @param message String contenant les informations de la raqket du nouveau joueur
     */
    public void addRacketNewPlayer(String message){
        String[] item = message.split(" ");
        Racket newRacket = new Racket(Protocol.decryptId(item));
        getPong().add(newRacket);
    }

    /**
     * Permet de lister et envoyer tout les éléments à un joueur
     *
     * @param socket Socket à qui envoyer les information
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
        /* creation nouvelle racket et envoie des items du jeu au nouveau joueur */
        Racket newRacket = new Racket(this.getNombrePlayer());
        String item = listItemGame(newRacket);
        this.getPong().add(newRacket);
        Thread.sleep(1);
        sendMessage(socket, item);
    }

    /**
     * Cette fonction permet de mettre à jour la ou les balle
     * ainsi que la raquette de celui qui à envoyer le message
     *
     * @param idSocket numero de la socket dans l'arrayList SocketPlayer
     * @throws IOException
     */
    public void update(int idSocket) throws IOException {
        InputStream is = this.getSocket(idSocket).getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String message = br.readLine();
        if (message != null) {
            String[] item = message.split(";");
            for (String anItem : item) {
                String[] info = anItem.split(" ");
                if (idPlayerControlBall(info) != idplayer)
                    updateItem(info, "Ball");
                updateItem(info, "Racket");
            }
        }
    }

    /**
     * Fonction permettant de mettre à jour un item grace à un message contenant ses information
     *
     * @param message tring contenant les information d'un item normalisé selon le protocole
     * @param type    String permettant de chercher un type spécifique de la liste des item ex: "Raquet"
     */
    public void updateItem(String[] message, String type) {
        int idP = Protocol.decryptId(message);
        int x = Protocol.decryptX(message);
        int y = Protocol.decryptY(message);
        for (int k = 0; k < getPong().listItemSize(); k++) {
            if (getPong().getItem(k).getClass().getSimpleName().equals(type)
                    && getPong().getItem(k).getNumber() == idP) {
                getPong().getItem(k).setPosition(x, y);
                getPong().getItem(k).setSpeed(Protocol.decryptSpeedX(message),
                        Protocol.decryptSpeedY(message));

            }
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

            this.initialisationItem(listItem);
            if (!tabSocket.isEmpty())
                this.initialisationSocket(tabSocket);
        }
        else{
            String myRacket = Protocol.informationItem(getPong().getItem(MYRACKET));
            Thread.sleep(1);
            sendMessage(getSocketPlayer(position), myRacket);
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
            addRacketNewPlayer(lu);
            this.addPlayer();
        }
    }


}


