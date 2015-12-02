package src.gui;

import src.Menu.CreatePongDialog;
import src.Menu.CreatePongDialogInfo;
import src.Menu.JoinPongDialog;
import src.Menu.JoinPongDialogInfo;
import src.Network.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Se Easy on 28/11/2015.
 */
public class Menu extends JFrame {
    private CreatePongDialogInfo cpdInfo;
    private JoinPongDialogInfo jpdInfo;
    private JButton create = new JButton("Créer un nouveau Pong");
    private JButton join = new JButton("Rejoindre Pong existant");
    private boolean createGame = false;
    private boolean joinGame = false;
    private Player client;
    private JDialog jd;

    public String getInfoCreate(){return cpdInfo.toString();}
    public String[] getInfoJoin(){return jpdInfo.toString().split(";");}
    public boolean getCreateGame(){return createGame;}
    public boolean getJoinGame(){return joinGame;}
    public Player getClient(){return this.client;}



    public Menu(Player client){
        this.client = client;
        this.setTitle("Pong");
        this.setSize(300, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new FlowLayout());
        this.getContentPane().add(create);
        this.jd = new JDialog(this,false);
        jd.setSize(new Dimension(300,150));

        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                createGame = true;
                CreatePongDialog cpd = new CreatePongDialog(null, "Créons un nouveau Pong", true);
                cpdInfo = cpd.showcpDialog();
                getClient().getPong().add(new Racket(1));
                getClient().getPong().add(new Ball(1));
                getClient().getPong().add(new Bonus());
                getClient().setMaxPlayer(Integer.parseInt(getInfoCreate()));
                endMenu();
                JLabel jl = null;
                try {
                    jl = new JLabel("<html><div style=\"text-align:center;\">" +
                            "En attente de joueurs<br><br> "+
                            "Adresse Connection: " +  getClient().getAdressServeur() + "<br> Port: "+ getClient().getPort() + "</div></html>",JLabel.CENTER);
                    jl.setSize(new Dimension(300,150));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                jd.add(jl);
                jd.setVisible(true);
            }

        });
        this.getContentPane().add(join);
        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JoinPongDialog jpd = new JoinPongDialog(null, " Rejoignons une partie existante ", true);
                jpdInfo = jpd.showjpDialog();
                joinGame = true;
                String adress = getInfoJoin()[0];
                int portConnection = Integer.parseInt(getInfoJoin()[1]);
                getClient().setNombrePlayer(1);
                try {
                    getClient().connectionServerInit(adress, portConnection, true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                getClient().getPong().addKeyListener(getClient().getMyRacket());
                endMenu();
                JLabel jl = new JLabel("Connection en cours");
                jd.add(jl);
                jd.setVisible(true);
            }
        });

        this.setVisible(true);
    }

    public void endMenu(){
        this.setVisible(false);
    }
    public void endWait(){
        jd.setVisible(false);
    }
}
