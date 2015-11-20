package src.gui;

import src.util.VariableStatic;
import java.io.*;
import java.net.Socket;

public class Player extends PlayerNetwork{

    private static int MYRACKET= 0;
    public Pong pong;
    public int idplayer;
    public int nombrePlayer;

    public int getNombrePlayer(){return nombrePlayer;}

    public Player(Pong pong) {
        super();
        this.pong = pong;
        idplayer = 1;
    }

    public void addPlayer() {
        this.nombrePlayer++;
    }

    /**
     * Permet de lister tout les items du jeu
     * @return String contenant tout les Items
     */
    private String listAllItem(Racket newRacket) {
        StringBuilder message = new StringBuilder();
        message.append(Protocol.attributionNewPlayer(this.nombrePlayer,this.nombrePlayer,newRacket));
        message.append(";");
        for (int i = 0; i < pong.listItemSize(); i++) {
            message.append(Protocol.InformationItem(pong.getItem(i)));
            message.append(";");
            }
        return message.toString();
    }

    /**
     * Permet d'initialiser un joueur en fonction d'un string reçu
     * @param message String contenant tout les objets du jeu
     */
    public void initialisation(String message)  {
        String[] listItem = message.split(";");
        String[] item = listItem[MYRACKET].split(" ");
        this.nombrePlayer = Protocol.decryptNumberPlayer(item);
        this.idplayer = Protocol.decryptId(item);
        pong.getItem(MYRACKET).setNumber(this.idplayer);
        for (int i = 0; i < listItem.length - 1; i++) {
            item = listItem[i].split(" ");
            if (updateItem(item, Protocol.decryptClasseItem(item))==VariableStatic.EXIT_FAILURE) {
                if (Protocol.decryptClasseItem(item).compareTo("Racket") == 0)
                    pong.add(new Racket(Protocol.decryptId(item),
                            Protocol.decryptX(item),
                            Protocol.decryptY(item)));
                else
                    pong.add(new Ball(Protocol.decryptId(item),
                            Protocol.decryptX(item),
                            Protocol.decryptY(item)));
            }
        }

    }

    /**
     * Permet de lister et envoyer tout les éléments à un joueur
     * @param socket Socket à qui envoyer les information
     * @throws IOException
     */
    public void addNewClient(Socket socket) throws IOException {
        /* envoie tout les objets présent dans le jeu */
        Racket newRacket = new Racket(2, 785, 0);
        this.addPlayer();
        String item = listAllItem(newRacket);
        this.pong.add(newRacket);
        sendMessage(socket,item);
    }

    /**
     * Permet de creer une chaine de caractère contenant les position de la raquette d'un joueur
     * ainsi que tout les positions des balles
     * @return String contenant les positions
     */
    public String Information() {
        StringBuffer message = new StringBuffer();
        message.append(Protocol.InformationItem(this.pong.getItem(MYRACKET))).append(";");
        for (int i = 0; i < pong.listItemSize(); i++) {
            if (pong.getItem(i) instanceof Ball){
                message.append(Protocol.InformationItem(pong.getItem(i))).append(";");
            }
        }
        return message.toString();
    }

    /**
     * Cette fonction permet de mettre à jour la ou les balle
     * ainsi que la raquette de celui qui à envoyer le message
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
                if (idPlayerControlerBall(info) != idplayer)
                    updateItem(info, "Ball");
                updateItem(info, "Racket");
            }
        }
    }

    /**
     * Permet de connaitre l'id du joueur contolant la balle
     * @param message String contenant les information d'une balle normalisé selon le protocole
     * @return retourne le numero du joueur controlant la balle
     */
    public int idPlayerControlerBall(String[] message){
        int x = Protocol.decryptX(message);
        int y = Protocol.decryptY(message);
        int idPlayer = 0;
        int distanceMin = -1;
        for (int k = 0; k < pong.listItemSize(); k++) {
            if (pong.getItem(k) instanceof Racket ) {
                int distance = (int)Math.sqrt( Math.pow((x-pong.getItem(k).getPositionX()),2) +
                        Math.pow((y-pong.getItem(k).getPositionY()),2));
                if (distance < distanceMin || distanceMin < 0) {
                    idPlayer = pong.getItem(k).getNumber();
                    distanceMin=distance;
                }
            }
        }
        return idPlayer;
    }

    /**
     * Fonction permettant de mettre à jour un item grace à un message contenant ses information
     * @param message tring contenant les information d'un item normalisé selon le protocole
     * @param type String permettant de chercher un type spécifique de la liste des item ex: "Raquet"
     * @return
     */
    public int updateItem (String[] message, String type){
        if (!message[0].equals(type))
            return VariableStatic.EXIT_FAILURE;
        int idP = Protocol.decryptId(message);
        int x = Protocol.decryptX(message);
        int y = Protocol.decryptY(message);
        for (int k = 0; k < pong.listItemSize(); k++) {
            if (pong.getItem(k).getClass().getSimpleName().equals(type)) {
                if (pong.getItem(k).getNumber() == idP) {
                    pong.getItem(k).setPosition(x,y);
                    pong.getItem(k).setSpeed(Protocol.decryptSpeedX(message),
                            Protocol.decryptSpeedY(message));
                    return VariableStatic.EXIT_SUCCESS;

                }
            }
        }
        return VariableStatic.EXIT_FAILURE;
    }

    /**
     * Fonction permettant de ce connecter à un serveur et d'initialisé le pong
     * @param adress addresse à se connecter
     * @param portConnection port de connection
     * @param first Vrai si première connection dans la partie Faux sinon
     * @throws IOException
     */
    public void connectionServerInit(String adress, int portConnection, boolean first) throws IOException {
        String info = super.connectionServer(adress,portConnection,first);
        /* initialisation de tout les objets grâce à l'information reçu */
        this.initialisation(info);
    }

    /**
     * Fonction permettant d'accepter une connexion d'un joueur et
     * de l'ajouter dans ca liste Item, et lui envoyer la liste des item deja présent dans le jeu
     * @param socket Socket à accepter
     * @return return si Connection établie et joueur valide retourne EXIT_SUCCESS sinon retourne EXIT_FAILURE
     * @throws IOException
     */
    public int connectionAccept(Socket socket) throws IOException {
        int position = super.connectionAccept(socket);
        if (position < 0)
            return VariableStatic.EXIT_FAILURE;
        this.addNewClient(this.getSocket(position));
        return VariableStatic.EXIT_SUCCESS;
    }

    public void getPoint(){
        for (int i = 0; i < pong.listItemSize() ; i++) {
            if(pong.getItem(i) instanceof Ball){
                Ball b =(Ball) pong.getItem(i);
                int player = b.getLosePlayerSize();
                if(player != 0 && player <= nombrePlayer ){
                    //tous les autres gagne un point!
                   b.restart();
                }
            }
        }
    }

}