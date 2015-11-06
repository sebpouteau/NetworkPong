package src;

/**
 * Created by seb on 05/11/15.
 */
public class test {
    private static void decrypt(String message){
        String[] tab = null;
        tab = message.split(" ");
        switch(tab[0]){
            case "add":
                System.out.println("add");
                break;
            case "Racket":
                System.out.println("racket");
                break;
            case "Ball":
                System.out.println("Ball");
        }

    }

    public static void main(String []args){
        decrypt("Racket bonjour");
    }

}
