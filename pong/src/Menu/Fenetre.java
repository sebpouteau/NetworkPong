package src.Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 * Created by ysaemery on 27/11/15.
 */
public class Fenetre extends JFrame{

    private JButton create = new JButton("Créer un nouveau Pong");
    private JButton join = new JButton("Rejoindre Pong existant");


  public Fenetre(){

    this.setTitle("Pong");
    this.setSize(300, 100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.getContentPane().setLayout(new FlowLayout());
    this.getContentPane().add(create);

    create.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            CreatePongDialog cpd = new CreatePongDialog(null, "Créons un nouveau Pong", true);
            CreatePongDialogInfo cpdInfo = cpd.showcpDialog();
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(null, cpdInfo.toString(), "Nombre de Joueurs", JOptionPane.INFORMATION_MESSAGE);
        }

    });
      this.getContentPane().add(join);
      join.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              JoinPongDialog jpd = new JoinPongDialog(null, "rejoignons la partie existante", true);
              JoinPongDialogInfo jpdInfo = jpd.showjpDialog();
              JOptionPane jop = new JOptionPane();
              jop.showMessageDialog(null, jpdInfo.toString(), "Nombre de Joueurs", JOptionPane.INFORMATION_MESSAGE);
          }
      });


    this.setVisible(true);

  }

// private JButton bouton = new JButton("Appel à la ZDialog");
//
//
//  public Fenetre(){
//
//    this.setTitle("Ma JFrame");
//    this.setSize(300, 100);
//    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    this.setLocationRelativeTo(null);
//    this.getContentPane().setLayout(new FlowLayout());
//    this.getContentPane().add(bouton);
//
//    bouton.addActionListener(new ActionListener(){
//
//      public void actionPerformed(ActionEvent arg0) {
//
//        ZDialog zd = new ZDialog(null, "Coucou les ZérOs", true);
//
//        ZDialogInfo zInfo = zd.showZDialog();
//
//        JOptionPane jop = new JOptionPane();
//
//        jop.showMessageDialog(null, zInfo.toString(), "Informations personnage", JOptionPane.INFORMATION_MESSAGE);
//
//      }
//
//    });
//
//    this.setVisible(true);
//
//  }

  public static void main(String[] main){

    Fenetre fen = new Fenetre();

  }
}
