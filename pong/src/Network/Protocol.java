package src.Network;

import src.gui.PongItem;
import src.gui.Racket;

/**
 * Created by sebpouteau on 18/11/15.
 */
public class Protocol {
    private static int CLASSE_ITEM = 0;
    private static int ID = 1;
    private static int POS_X = 2;
    private static int POS_Y = 3;
    private static int SPEED_X = 4;
    private static int SPEED_Y = 5;
    private static int NUMBER_PLAYER = 6;
    private static int SET_POSSIBILITY_BONUS = 6;
    private static int MAX_PLAYER = 7;
    private static int ID_PLAYER_CONNECTED = 8;
    private static int IDENTIFICATION=0;
    private static int POS_INFORM_IDENTIFICATION = 1;
    private static int PORT = 1;
    private static int FIRST = 2;
    private static int LENGTH_IDENTIFICATION=3;


    /* =================================================
                   Decrypteur de message
      ================================================= */

    /**
     * Permet de recupérer l'adresse de connection d'une socket contenue dans le String
     * @param message String contenant un Item
     * @return Adresse de connection
     */
    public static String decryptAdress(String[] message){
        return message[2];
    }

    /**
     * Permet de recupérer le port de connection d'une socket contenu dans le String
     * @param message String contenant un Item
     * @return Port de connection
     */
    public static int decryptPortSocket(String[] message){
        return Integer.parseInt(message[1]);
    }

    /**
     * Permet de recupérer le port de connection d'une socket contenu dans le String de connection
     * @param message String contenant un Item
     * @return Port de connection
     */
    public static int decryptPort(String message) {
        String[] tabMessage = message.split(";");
        String[] infoPort = tabMessage[PORT].split(" ");
        return Integer.parseInt(infoPort[POS_INFORM_IDENTIFICATION]);
    }

    /**
     * Permet de recupérer le boolean permetant de savoir si c'est la premiere connection avec
     * un joueur du jeu ou non
     * @param message String contenant un Item
     * @return true si premiere connexion, false sinon
     */
    public static boolean decryptFirst(String message){
        String[] tabMessage = message.split(";");
        String[] infoFirst = tabMessage[FIRST].split(" ");
        return Boolean.parseBoolean(infoFirst[POS_INFORM_IDENTIFICATION]);
    }

    /**
     * Permet de savoir le nom de la classe de l'item contenu dans le String
     * @param message String contenant un Item
     * @return nom de la classe
     */
    public static String decryptClasseItem(String[] message){
        return message[CLASSE_ITEM];
    }

    /**
     * Permet de recupérer l'ID de l'item contenu dans le String
     * @param message String contenant un Item
     * @return ID de l'item
     */
    public static int decryptId(String[] message){
        return Integer.parseInt(message[ID]);
    }

    /**
     * Permet de recupérer la position X de l'item contenu dans le String
     * @param message String contenant un Item
     * @return Position X de l'item
     */
    public static int decryptX(String[] message){
        return Integer.parseInt(message[POS_X]);
    }

    /**
     * Permet de recupérer la position Y de l'item contenu dans le String
     * @param message String contenant un Item
     * @return Position Y de l'item
     */
    public static int decryptY(String[] message){
        return Integer.parseInt(message[POS_Y]);

    }

    /**
     * Permet de recupérer la vitesse en X de l'item contenu dans le String
     * @param message String contenant un Item
     * @return la vitesse en X de l'item
     */
    public static int decryptSpeedX(String[] message){
        return Integer.parseInt(message[SPEED_X]);

    }

    /**
     * Permet de recupérer la vitesse en Y de l'item contenu dans le String
     * @param message String contenant un Item
     * @return la vitesse en Y de l'item
     */
    public static int decryptSpeedY(String[] message){
        return Integer.parseInt(message[SPEED_Y]);

    }

    /**i
     * Permet de recupérer le nombre de joueur valable que pour la phase d'initialisation d'un objet
     * @param message String contenant L'initialisaton d'un joueur
     * @return le nombre de joueur
     */
    public static int decryptNumberPlayer(String[] message){
        return Integer.parseInt(message[NUMBER_PLAYER]);
    }

    public static boolean decryptSetPossibilityBonus(String[] message) { return Boolean.parseBoolean(message[SET_POSSIBILITY_BONUS]);}
    /**i
     * Permet de recupérer le nombre de joueur valable que pour la phase d'initialisation d'un objet
     * @param message String contenant L'initialisaton d'un joueur
     * @return le nombre de joueur
     */
    public static int decryptMaxPlayer(String[] message){
        return Integer.parseInt(message[MAX_PLAYER]);
    }

    public static int decryptIdPlayerConnected(String[] message){ return Integer.parseInt(message[ID_PLAYER_CONNECTED]);}

    /* =================================================
                    Fonction Utile
       ================================================= */

    /**
     * Permet de verifier que le joueur est valide
     * @param message String contenant le message de connection.
     * @return true si joueur valide, false sinon
     */
    public static boolean validPlayer(String message) {
        String[] tabMessage = message.split(";");
        return tabMessage[IDENTIFICATION].compareTo("Pong Play") == 0
                && tabMessage.length == LENGTH_IDENTIFICATION;
    }

    /**
     * Génère le message valide servant à la connection à un serveur
     * @param port Port du Serveur du joueur qui veux se connecter
     * @param firstConnection true si première connection, false sinon
     * @return retourne le message à envoyer au serveur
     */
    public static String identification(int port, boolean firstConnection){
        return  "Pong Play;Port: " + port + ";ConnectionFirst " + firstConnection;
    }

    /**
     * Génère le String contenant les information de la raquette du nouveau joueur
     * ainsi que le nombre de joueur
     * @param numberPlayer nombre de joueur dans la partie
     * @param racket Racket du nouveau joueur
     * @return String contenant les informations
     */
    public static String attributionNewPlayer(int numberPlayer, int maxPlayer, Racket racket, int idPlayerConncted){
        StringBuilder m = new StringBuilder();
        m.append(informationItem(racket)).append(" ");
        m.append(numberPlayer).append(" ");
        m.append(maxPlayer).append(" ");
        m.append(idPlayerConncted);
        return m.toString();
    }

    /**
     * Génère un String contenant toute les informations d'un item
     * @param item Item dont on veux les informations
     * @return message contenant tout les informations sur l'item
     */
    public static String informationItem(PongItem item){
        StringBuilder m = new StringBuilder();
        m.append(item.getClass().getSimpleName()).append(" ");
        m.append(item.getNumber()).append(" ");
        m.append(item.getPositionX()).append(" ");
        m.append(item.getPositionY()).append(" ");
        m.append(item.getSpeedX()).append(" ");
        m.append(item.getSpeedY());
        return m.toString();
    }

    public static String informationBonus(PongItem item,boolean setPossible){
        StringBuilder m = new StringBuilder();
        m.append(informationItem(item)).append(" ");
        m.append(setPossible).append(";");
        return m.toString();
    }

    /**
     * Génère les informations sur une socketPlayer
     * @param socketPlayer SocketPlayer contenant une connection vers un autre joueur
     * @return String contenant tout les informations
     */
    public static String infomationSocket(SocketPlayer socketPlayer){
        StringBuilder m = new StringBuilder();
        m.append("Socket ");
        m.append(socketPlayer.getPort()).append(" ");
        m.append(socketPlayer.getAdress()).append(";");
        return m.toString();
    }

}
