package src.gui;

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

    public static String MESSAGE_END = "FIN";

    public static String decryptClasseItem(String[] message){
        return message[CLASSE_ITEM];
    }

    public static int decryptId(String[] message){
        return Integer.parseInt(message[ID]);
    }
    public static int decryptX(String[] message){
        return Integer.parseInt(message[POS_X]);
    }
    public static int decryptY(String[] message){
        return Integer.parseInt(message[POS_Y]);

    }
    public static int decryptSpeedX(String[] message){
        return Integer.parseInt(message[SPEED_X]);

    }
    public static int decryptSpeedY(String[] message){
        return Integer.parseInt(message[SPEED_Y]);

    }
    public static int decryptNumberPlayer(String[] message){
        return Integer.parseInt(message[NUMBER_PLAYER]);

    }

    public static boolean validPlayer(String message) {
        String[] tabMessage = message.split(";");
        return tabMessage[0].compareTo("Pong Play") == 0 && tabMessage.length == 3;
    }

    public static int decryptPort(String message) {
        String[] tabMessage = message.split(";");
        String[] infoPort = tabMessage[1].split(" ");
        return Integer.parseInt(infoPort[1]);

    }

    public static String identification(int port, boolean firstConnection){
        return  "Pong Play;Port: " + port + ";ConnectionFirst " + firstConnection;
    }

    public static String attributionNewPlayer(int idPlayer, int numberPlayer, Racket racket){
        StringBuffer m = new StringBuffer();
        m.append(InformationItem(racket));
        m.append(numberPlayer);
        return m.toString();
    }
    //public static
    public static String InformationItem(PongItem item){
        StringBuffer m = new StringBuffer();
        m.append(item.getClass().getSimpleName()).append(" ");
        m.append(item.getNumber()).append(" ");
        m.append(item.getPositionX()).append(" ");
        m.append(item.getPositionY()).append(" ");
        m.append(item.getSpeedX()).append(" ");
        m.append(item.getSpeedY()).append(" ");
        return m.toString();
    }



}
