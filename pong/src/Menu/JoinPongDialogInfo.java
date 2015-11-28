package src.Menu;

/**
 * Created by ysaemery on 27/11/15.
 */
public class JoinPongDialogInfo  {
    private String adress;
    private String port;

    public JoinPongDialogInfo(){}

    public JoinPongDialogInfo(String adress, String port){
        this.adress = adress;
        this.port = port;
    }

    public String toString(){
        String str;
        if(this.adress != null && this.port != null) {

            str = adress;
            str += ";";
            str += port;
        }
        else
            str =null;
        return str;
    }
}
