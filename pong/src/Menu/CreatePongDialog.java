package src.Menu;

import src.Menu.CreatePongDialogInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ysaemery on 27/11/15.
 */
public class CreatePongDialog extends JDialog {

    private CreatePongDialogInfo cpdinfo = new CreatePongDialogInfo();
    private JTextField numberPlayerTextField;
    private boolean sendData;
    private JLabel numberPlayerLabel;


public CreatePongDialog(JFrame parent, String title, boolean modal){
    super(parent, title, modal);
    this.setSize(300,200);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    this.initComponent();
}

    public CreatePongDialogInfo showcpDialog(){
        this.sendData = false;
        this.setVisible(true);
        return cpdinfo;
    }


    private void initComponent(){

        JPanel panNumberPlayer = new JPanel();
        panNumberPlayer.setBackground(Color.white);
        panNumberPlayer.setPreferredSize(new Dimension(300,80));
        numberPlayerTextField = new JTextField();
        numberPlayerTextField.setPreferredSize(new Dimension(50,25));
        panNumberPlayer.setBorder(BorderFactory.createTitledBorder("Nombre de joueurs"));
        numberPlayerLabel = new JLabel("Saisir le nombre de joueurs (max 4)");

        panNumberPlayer.add(numberPlayerLabel);
        panNumberPlayer.add(numberPlayerTextField);

        JPanel content = new JPanel();
        content.setBackground(Color.white);
        content.add(panNumberPlayer);

        JPanel control = new JPanel();
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cpdinfo = new CreatePongDialogInfo(numberPlayerTextField.getText());
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
