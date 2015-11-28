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
    private JOptionPane jop;

    public String getInfoCreate(){return cpdInfo.toString();}
    public String[] getInfoJoin(){return jpdInfo.toString().split(";");}
    public boolean getCreateGame(){return createGame;}
    public boolean getJoinGame(){return joinGame;}
    public Player getClient(){return this.client;}
    public void setJop(JOptionPane jop){this.jop = jop;}
    public JOptionPane getJop(){return jop;}


    public Menu(Player client){
        this.client = client;
        this.setTitle("Pong");
        this.setSize(300, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new FlowLayout());
        this.getContentPane().add(create);
        this.jop = new JOptionPane();

        create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                createGame = true;
                System.out.println(getCreateGame());
                CreatePongDialog cpd = new CreatePongDialog(null, "Créons un nouveau Pong", true);
                cpdInfo = cpd.showcpDialog();
                getClient().getPong().add(new Racket(1));
                getClient().getPong().add(new Ball(1, 80, 80));
                getClient().getPong().add(new Bonus());
                getClient().setMaxPlayer(Integer.parseInt(getInfoCreate()));
              //  getJop().showMessageDialog(null, "En attente de joueurs");
                System.out.println("je crée une partie");
            }

        });
        this.getContentPane().add(join);
        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JoinPongDialog jpd = new JoinPongDialog(null, "rejoignons la partie existante", true);
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
              //  getJop().showMessageDialog(null, "Connection en cours");
            }
        });

        this.setVisible(true);
    }

    public void endMenu(){
        this.setVisible(false);
    if(getJop() != null) {
        System.out.println("qje ferme ma fenêtre");
        getJop().setVisible(false);
    }
    }
}
