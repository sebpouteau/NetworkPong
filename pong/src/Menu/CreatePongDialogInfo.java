package src.Menu;

/**
 * Created by ysaemery on 27/11/15.
 */
public class CreatePongDialogInfo {
    private String numberPlayer;

    public CreatePongDialogInfo(){}

    public CreatePongDialogInfo(String numberPlayer){
        this.numberPlayer = numberPlayer;

    }

    public String toString(){
        String str;
        if(this.numberPlayer != null) {
         //   str = "Nombre de joueurs :";
            str = numberPlayer;
        }
        else
            str = "Aucune Information !";
        return str;
    }
}
