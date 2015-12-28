package src.gui;

import src.Network.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Menu extends JFrame implements ActionListener {

    private JPanel contener = new JPanel();
    private JPanel text = new JPanel();

    private JButton hosting = new JButton("Héberger un Pong");
    private JButton join = new JButton("Rejoindre Pong");
    private JButton createGame = new JButton("Valider");
    private JButton joinGame = new JButton("Valider");

    private JTextField numberPlayerTextField = new JTextField();
    private JTextField adressJPTextField = new JTextField();
    private JTextField portTextField = new JTextField();

    private Player client;
    private int buttonTextSize = 15;
    private boolean allPlayerConnect = true;

    private final Dimension MENU_SIZE = new Dimension(300,250);
    private static final Color backgroundColor = new Color(144, 148, 201);

    public Menu(Player client) {
        this.client = client;
        this.setTitle("Pong");
        this.setSize(MENU_SIZE);
        this.getContentPane().add(contener);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        contener.setBackground(backgroundColor);
        text.setBackground(backgroundColor);

        modificationButton(hosting,buttonTextSize);
        modificationButton(join,buttonTextSize);
        modificationButton(createGame,buttonTextSize);
        modificationButton(joinGame,buttonTextSize);
    }

    /* ================================================
                      Getter and Setter
       ================================================ */

    public boolean isAllPlayerConnect() {
        return allPlayerConnect;
    }

    public void setAllPlayerConnect(boolean allPlayerConnect) {
        this.allPlayerConnect = allPlayerConnect;
    }

    public Player getClient() {
        return this.client;
    }

    /* ================================================
                         Functions
       ================================================ */

    private final Font fontTitle = new Font("Arial",Font.BOLD,40);
    private final Font fontText= new Font("Arial",Font.ITALIC,20);
    private final Font fontConnection= new Font("Arial",Font.BOLD,17);
    
    private final Dimension dimensionButton = new Dimension(200,30);
    private final Dimension dimensionAction = new Dimension(300,200);

    /**
     * Change la taille par défaut du bouton et sa police
     * @param button le bouton que l'on modifie
     * @param textSize la taille du texte sur le bouton
     */
    private void modificationButton(JButton button, int textSize){
        button.setPreferredSize(dimensionButton);
        button.setFont(new Font("Arial",Font.BOLD,textSize));
    }

    /**
     *
     */
    public void menuMain(){
        JLabel titre = new JLabel("PONG");
        
        text.setPreferredSize(new Dimension((int)MENU_SIZE.getWidth(),70));
        titre.setFont(fontTitle);
        text.add(titre,BorderLayout.CENTER);

        contener.add(text) ;
        contener.add(hosting);
        contener.add(join);

        hosting.addActionListener(this);
        join.addActionListener(this);

        this.setVisible(true);
    }

    /**
     * Fais disparaitre le menu de l'ecran.
     */
    public void endMenu() {
        this.setVisible(false);
    }

    /**
     *
     */
    private void initHosting() {
        this.setSize(dimensionAction);
        text.setPreferredSize(new Dimension (250,100));
        numberPlayerTextField.setPreferredSize(new Dimension(50, 25));
        JLabel numberPlayerLabel = new JLabel("<html><div style=\"text-align:center;\"> Saisir le nombre de joueurs<br> (max 4)<br></div></html>");
        numberPlayerLabel.setFont(fontText);

        text.setLayout(new BorderLayout());
        text.add(numberPlayerLabel,BorderLayout.LINE_START);
        text.add(numberPlayerTextField,BorderLayout.AFTER_LAST_LINE);
        createGame.addActionListener(this);
        contener.add(text);
        contener.add(createGame);
    }

    /**
     *
     */
    private void initJoin() {
        this.setSize(dimensionAction);
        JLabel adressLabel, portLabel;

        JPanel panAdress = new JPanel();
        panAdress.setBackground(backgroundColor);
        adressJPTextField.setPreferredSize(new Dimension(100,25));
        panAdress.setBorder(BorderFactory.createTitledBorder("Adresse de connection"));
        adressLabel = new JLabel("Saisir l'adresse de connection:");
        panAdress.setLayout(new BorderLayout());

        JPanel panPort = new JPanel();
        panPort.setBackground(backgroundColor);
        portTextField.setPreferredSize(new Dimension(50,25));
        panPort.setBorder(BorderFactory.createTitledBorder("Port de connection"));
        portLabel = new JLabel("Saisir le port de connection");

        panAdress.add(adressLabel,BorderLayout.LINE_START);
        panAdress.add(adressJPTextField,BorderLayout.AFTER_LAST_LINE);
        panPort.add(portLabel);
        panPort.add(portTextField);

        contener.add(panAdress);
        contener.add(panPort);
        joinGame.addActionListener(this);
        contener.add(joinGame);
    }

    /**
     *
     */
    private void displayInformationConnection(){
        JLabel jl = null;
        try {
            jl = new JLabel("<html><div style=\"text-align:center;\">" +
                    "En attente de joueurs<br><br> "+
                    "Adresse Connection: " +  getClient().getAdressServeur() + "<br> Port: "+ getClient().getPort() + "</div></html>",JLabel.CENTER);
            jl.setFont(fontConnection);
            jl.setSize(new Dimension(300,150));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contener.setLayout(new BorderLayout());
        contener.add(jl,BorderLayout.CENTER);
    }

    /**
     *
     */
    private void displayWaitPlayerHost(){
        getClient().getPong().add(new Racket(1));
        getClient().getPong().add(new Ball(1));
        getClient().getPong().add(new Bonus());

        int numberPlayer = Integer.parseInt(numberPlayerTextField.getText());
        getClient().setMaxPlayer(numberPlayer);

        displayInformationConnection();
    }

    /**
     *
     */
   private void displayWaitPlayerJoin(){
       String adress = adressJPTextField.getText();
       int portConnection = Integer.parseInt(portTextField.getText());
       getClient().setNumberPlayer(1);

       try {
           getClient().connectionServerInit(adress, portConnection, true);
       } catch (IOException e) {
           e.printStackTrace();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       getClient().getPong().addKeyListener((Racket)getClient().getMyRacket());
       displayInformationConnection();
       this.repaint();
   }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void waitPlayer() throws IOException, InterruptedException {
        while (client.getNumberPlayer() != client.getMaxPlayer() ) {
            if (client.getServer() != null) {
                SocketChannel sc = client.getServer().accept();
                if (sc != null) {
                    sc.socket().setTcpNoDelay(true);
                    client.connectionAcceptPlayer(sc.socket());
                }
            }
        }
        setAllPlayerConnect(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contener.removeAll();
        text.removeAll();
        if (e.getSource() == hosting) {
            initHosting();
        }
        if (e.getSource() == join) {
            initJoin();
        }
        if (e.getSource() == createGame) {
            if (numberPlayerTextField.getText().length() == 0){
                initHosting();
            }
            else {
                int numberPlayer = Integer.parseInt(numberPlayerTextField.getText());
                if (1 < numberPlayer && numberPlayer <= 4) {
                    displayWaitPlayerHost();
                }
                else{
                    initHosting();
                }
            }
        }
        if (e.getSource() == joinGame) {
            displayWaitPlayerJoin();
        }
        this.repaint();
        this.validate();
    }

    /** Affiche le menu.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void displayMenu() throws IOException, InterruptedException {
        menuMain();
        waitPlayer();
    }
}
