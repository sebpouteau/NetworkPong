package src.Network;

import src.Game.PongItem;
import src.Game.Racket;

/**
 * Protocol de communication.
 * Permet de generer les méssages et de les decryter.
 */
public class Protocol {
    
    private static int CLASSE_ITEM = 0;
    private static int ID = 1;
    private static int POS_X = 2;
    private static int POS_Y = 3;
    private static int SPEED_X = 4;
    private static int SPEED_Y = 5;
    private static int SCORE_PLAYER = 2;
    private static int NUMBER_PLAYER = 6;
    private static int MAX_PLAYER = 7;
    private static int ID_PLAYER_CONNECTED = 8;
    private static int MAX_SCORE = 9;
    private static int IDENTIFICATION=0;
    private static int POS_INFORM_IDENTIFICATION = 1;
    private static int PORT = 1;
    private static int ADDRESS = 2;
    private static int FIRST = 2;
    private static int LENGTH_IDENTIFICATION=3;

    /* ================================================
                   Decrypteur de message
       ================================================ */

    /**
     * Recupere l'adresse de connection d'une socket contenu dans le String.
     * @param message String contenant un Item.
     * @return Adresse de connection.
     */
    public static String decryptAddress(String[] message) {
        return message[ADDRESS];
    }

    /**
     * Recupere le port de connection d'une socket contenu dans le String.
     * @param message String contenant un Item.
     * @return Port de connection.
     */
    public static int decryptPortSocket(String[] message) {
        return Integer.parseInt(message[PORT]);
    }

    /**
     * Recupere le port de connection d'une socket contenu dans le String de connection.
     * @param message String contenant un Item.
     * @return Port de connection.
     */
    public static int decryptPort(String message) {
        String[] tabMessage = message.split(";");
        String[] infoPort = tabMessage[PORT].split(" ");
        return Integer.parseInt(infoPort[POS_INFORM_IDENTIFICATION]);
    }

    /**
     * Recupere le boolean permettant de savoir si c'est la première connection
     * avec un joueur du jeu ou non.
     * @param message String contenant un Item.
     * @return True si premiere connexion, False sinon.
     */
    public static boolean decryptFirst(String message) {
        String[] tabMessage = message.split(";");
        String[] infoFirst = tabMessage[FIRST].split(" ");
        return Boolean.parseBoolean(infoFirst[POS_INFORM_IDENTIFICATION]);
    }

    /**
     * Permet de savoir le nom de la classe de l'item contenu dans le String.
     * @param message String contenant un Item.
     * @return Nom de la classe.
     */
    public static String decryptClasseItem(String[] message){
        return message[CLASSE_ITEM];
    }

    /**
     * Recupere l'ID de l'item contenu dans le String.
     * @param message String contenant un Item.
     * @return ID de l'item.
     */
    public static int decryptId(String[] message){
        return Integer.parseInt(message[ID]);
    }

    /**
     * Recupere la position X de l'item contenu dans le String.
     * @param message String contenant un Item.
     * @return Position X de l'item.
     */
    public static int decryptX(String[] message){
        return Integer.parseInt(message[POS_X]);
    }

    /**
     * Recupere la position Y de l'item contenu dans le String.
     * @param message String contenant un Item.
     * @return Position Y de l'item.
     */
    public static int decryptY(String[] message){
        return Integer.parseInt(message[POS_Y]);
    }

    /**
     * Recupere la vitesse en X de l'item contenu dans le String.
     * @param message String contenant un Item.
     * @return La vitesse en X de l'item.
     */
    public static int decryptSpeedX(String[] message){
        return Integer.parseInt(message[SPEED_X]);
    }

    /**
     * Recupere la vitesse en Y de l'item contenu dans le String.
     * @param message String contenant un Item.
     * @return La vitesse en Y de l'item.
     */
    public static int decryptSpeedY(String[] message) {
        return Integer.parseInt(message[SPEED_Y]);
    }

    /**
     * Recupere le nombre de joueurs valables pour la phase d'initialisation d'un objet.
     * @param message String contenant l'initialisaton d'un joueur.
     * @return Le nombre de joueurs.
     */
    public static int decryptNumberPlayer(String[] message){
        return Integer.parseInt(message[NUMBER_PLAYER]);
    }

    /**
     * Recupere le nombre de joueurs valables, que lors de la phase d'initialisation d'un objet.
     * @param message String contenant l'initialisaton d'un joueur.
     * @return Le nombre de joueurs.
     */
    public static int decryptMaxPlayer(String[] message){
        return Integer.parseInt(message[MAX_PLAYER]);
    }

    /**
     * Recupere le numero du joueur qui c'est connecte.
     * @param message String contenant l'initialisation du joueur.
     * @return Le numero du joueur qui se connecte.
     */
    public static int decryptIdPlayerConnected(String[] message) {
        return Integer.parseInt(message[ID_PLAYER_CONNECTED]);
    }

    /**
     * Decrypte le score d'un message.
     * @param message Message contenant le score.
     * @return Le score contenu dans le message.
     */
    public static int decryptScorePlayer(String[] message){
        return Integer.parseInt(message[SCORE_PLAYER]);
    }

    /**
     * Decrypte le score maximal contenu dans le message.
     * @param message Message contenant le score maximal.
     * @return Le score maximal.
     */
    public static int decryptMaxScore(String[] message) {
        return Integer.parseInt(message[MAX_SCORE]);
    }


    /* ================================================
                    Createur de message
       ================================================ */

    /**
     * Verifie que le joueur est valide.
     * @param message String contenant le message de connection.
     * @return True si joueur valide, False sinon.
     */
    public static boolean validPlayer(String message) {
        String[] tabMessage = message.split(";");
        return tabMessage[IDENTIFICATION].compareTo("Pong Play") == 0
                && tabMessage.length == LENGTH_IDENTIFICATION;
    }

    /**
     * Genere le message valide servant a la connection à un serveur.
     * @param port Port du Serveur du joueur qui veut se connecter.
     * @param firstConnection True si premiere connection, False sinon.
     * @return Retourne le message a envoyer au serveur.
     */
    public static String identification(int port,boolean firstConnection) {
        return  "Pong Play;Port: " + port + ";ConnectionFirst: " + firstConnection;
    }

    /**
     * Genere le String contenant les informations de la raquette du nouveau joueur
     * ainsi que le nombre de joueurs.
     * @param numberPlayer Nombre de joueurs dans la partie.
     * @param racket Raquette du nouveau joueur.
     * @return String contenant les informations.
     */
    public static String attributionNewPlayer(int numberPlayer, int maxPlayer, Racket racket, int idPlayerConncted, int maxScore) {
        StringBuilder m = new StringBuilder();
        m.append(informationItem(racket)).append(" ");
        m.append(numberPlayer).append(" ");
        m.append(maxPlayer).append(" ");
        m.append(idPlayerConncted).append(" ");
        m.append(maxScore).append(" ");
        return m.toString();
    }

    /**
     * Genere un String contenant toutes les informations d'un PongItem.
     * @param item PongItem dont on veux les informations.
     * @return Message contenant toutes les informations sur item.
     */
    public static String informationItem(PongItem item) {
        StringBuilder m = new StringBuilder();
        m.append(item.getClass().getSimpleName()).append(" ");
        m.append(item.getNumber()).append(" ");
        m.append(item.getPositionX()).append(" ");
        m.append(item.getPositionY()).append(" ");
        m.append(item.getSpeedX()).append(" ");
        m.append(item.getSpeedY());
        return m.toString();
    }

    /**
     * Genere les informations sur une socketPlayer.
     * @param socketPlayer SocketPlayer contenant une connection vers un autre joueur.
     * @return String contenant toutes les informations.
     */
    public static String infomationSocket(SocketPlayer socketPlayer) {
        StringBuilder m = new StringBuilder();
        m.append("Socket ");
        m.append(socketPlayer.getPort()).append(" ");
        m.append(socketPlayer.getAddress()).append(";");
        return m.toString();
    }

    /**
     * Genere un message contenant le score.
     * @param player Un joueur.
     * @return Un message contenant le score.
     */
    public static String informationScore(Player player) {
        StringBuilder message = new StringBuilder();
        message.append("Score ");
        message.append(String.valueOf(player.getIdPlayer())).append(" ");
        message.append(String.valueOf(player.getPong().getScore(player.getIdPlayer())));
        return message.toString();
    }

    /**
     * Genere le message contenant l'identifiant du joueur.
     * @param id Numero du joueur.
     * @return Message contenant l'identifiant du joueur.
     */
    public static String idPlayer(int id){
        return "IdPlayer "+ id +";";
    }
}
