package src.Menu;

import src.Menu.JoinPongDialogInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ysaemery on 27/11/15.
 */
public class JoinPongDialog extends JDialog {

    private JoinPongDialogInfo jpdinfo = new JoinPongDialogInfo();
    private JTextField adressJPTextField, portTextField;
    private boolean sendData;
    private JLabel adressLabel, portLabel;

    public JoinPongDialog(JFrame parent, String title, boolean modal){
        super(parent, title, modal);
        this.setSize(300,300);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.initComponent();
    }

    public JoinPongDialogInfo showjpDialog(){
        this.sendData = false;
        this.setVisible(true);
        return jpdinfo;
    }


    private void initComponent(){

        JPanel panAdress = new JPanel();
        panAdress.setBackground(Color.white);
        panAdress.setPreferredSize(new Dimension(300,60));
        adressJPTextField = new JTextField();
        adressJPTextField.setPreferredSize(new Dimension(50,25));
        panAdress.setBorder(BorderFactory.createTitledBorder("Adresse de connection"));
        adressLabel = new JLabel("Saisir l'adresse de connection");

        panAdress.add(adressLabel);
        panAdress.add(adressJPTextField);

        JPanel panPort = new JPanel();
        panPort.setBackground(Color.white);
        panPort.setPreferredSize(new Dimension(300,60));
        portTextField = new JTextField();
        portTextField.setPreferredSize(new Dimension(50,25));
        panPort.setBorder(BorderFactory.createTitledBorder("Port de connection"));
        portLabel = new JLabel("Saisir le de connection");

        panPort.add(portLabel);
        panPort.add(portTextField);

        JPanel content = new JPanel();
        content.setBackground(Color.white);
        content.add(panAdress);
        content.add(panPort);

        JPanel control = new JPanel();
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpdinfo = new JoinPongDialogInfo(adressJPTextField.getText(), portTextField.getText());
                setVisible(false);
            }
        });
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        control.add(okButton);
        control.add(cancelButton);

        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

}
