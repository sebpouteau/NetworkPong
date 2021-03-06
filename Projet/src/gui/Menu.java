package src.gui;


import src.Game.Ball;
import src.Game.Bonus;
import src.Game.Racket;
import src.Network.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;

public class Menu extends JFrame implements ActionListener {

    private JPanel contener = new JPanel() {
        /* Creer un Jpanel degrade de noir */
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, Color.BLACK, 1200, 1200, Color.WHITE, true);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    };

    private JPanel text = new JPanel();

    private JButton hosting = new JButton("Créer un Pong");
    private JButton join = new JButton("Rejoindre un Pong");
    private JButton createGame = new JButton("Valider");
    private JButton joinGame = new JButton("Valider");
    private JButton back = new JButton("Retour");

    private ButtonGroup scoreButtonGroup = new ButtonGroup();
    private JRadioButton limitedScore = new JRadioButton("Score max:");
    private JRadioButton unlimitedScore = new JRadioButton("Sans fin");

    private JComboBox  numberPlayerCombo = new JComboBox();

    private JTextField addressTextField = new JTextField();
    private JTextField portTextField = new JTextField();
    private JTextField scoreMaxTextField = new JTextField();

    private Player client;
    private int buttonTextSize = 25;
    private boolean allPlayerConnect = true;

    private final Dimension MENU_SIZE = new Dimension(500,400);
    private final int PREFERED_GAP = 40;

    private final Font fontTitle = new Font("Serif",Font.BOLD|Font.ITALIC,50);
    private final Font fontText = new Font("Arial",Font.PLAIN,25);
    private final Font fontConnection = new Font("Arial",Font.BOLD,25);

    private final Dimension dimensionButtonMenu = new Dimension(400,60);
    private final Dimension dimensionButton = new Dimension(200,50);


    public Menu(Player client) {
        this.client = client;
        this.setTitle("Pong");
        this.setSize(MENU_SIZE);
        this.getContentPane().add(contener);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        setJButton(hosting,buttonTextSize,dimensionButtonMenu);
        setJButton(join,buttonTextSize,dimensionButtonMenu);
        setJButton(createGame,buttonTextSize, dimensionButton);
        setJButton(joinGame,buttonTextSize, dimensionButton);
        setJButton(back, buttonTextSize, dimensionButton);

        numberPlayerCombo.addItem("2");
        numberPlayerCombo.addItem("3");
        numberPlayerCombo.addItem("4");

        hosting.addActionListener(this);
        join.addActionListener(this);
        createGame.addActionListener(this);
        joinGame.addActionListener(this);
        back.addActionListener(this);
        unlimitedScore.addActionListener(this);
        limitedScore.addActionListener(this);

        setRadioButton(unlimitedScore,false,Color.WHITE,fontText);
        setRadioButton(limitedScore, false, Color.WHITE,fontText);

        scoreButtonGroup.add(unlimitedScore);
        scoreButtonGroup.add(limitedScore);
        unlimitedScore.setSelected(true);

        scoreMaxTextField.setEnabled(false);
        scoreMaxTextField.setVisible(false);
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

    /**
     * Affiche le menu du jeu
     */
    public void menuMain() {
        JLabel title = new JLabel("PONG");
        setJLabel(title, fontTitle, Color.WHITE);
        text.setOpaque(false);
        text.setPreferredSize(new Dimension((int)MENU_SIZE.getWidth(),100));
        text.add(title,BorderLayout.CENTER);

        GroupLayout groupLayout = new GroupLayout(contener);
        contener.setLayout(groupLayout);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(text, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(hosting, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(join, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addContainerGap(PREFERED_GAP, PREFERED_GAP)
                        .addComponent(text , GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(hosting, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(PREFERED_GAP)
                        .addComponent(join, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        this.setVisible(true);
    }

    /**
     * Masquera la fenetre menu.
     */
    public void endMenu() {
        this.setVisible(false);
    }

    /**
     * Menu concernant la creation d'une partie.
     */
    private void initHosting() {
        clearWindow();

        JLabel numberPlayerLabel = new JLabel("<html><div style=\"text-align:center;\"> Saisir le nombre de joueurs<br>(max 4)</div></html>");
        setJLabel(numberPlayerLabel, fontText, Color.WHITE);

        numberPlayerCombo.setPreferredSize(new Dimension(50,30));
        numberPlayerCombo.setFont(fontText);

        setJTextField(scoreMaxTextField, 100, 30, fontText);

        GroupLayout groupLayout = new GroupLayout(contener);
        contener.setLayout(groupLayout);
        groupLayout.setLayoutStyle(new LayoutStyle() {
            @Override
            public int getPreferredGap(JComponent component1, JComponent component2, ComponentPlacement type, int position, Container parent) {
                return 20;
            }

            @Override
            public int getContainerGap(JComponent component, int position, Container parent) {
                return 100;
            }
        });
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
                        .addComponent(numberPlayerLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(numberPlayerCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(unlimitedScore, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(limitedScore, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(scoreMaxTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(createGame)
                                .addComponent(back))
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addContainerGap(PREFERED_GAP, PREFERED_GAP*2)
                        .addComponent(numberPlayerLabel , GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(numberPlayerCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(PREFERED_GAP)
                        .addComponent(unlimitedScore,  GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(limitedScore, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(scoreMaxTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(PREFERED_GAP)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(createGame)
                                .addComponent(back))
        );
    }

    /**
     * Menu concernant la partie permettant de rejoindre une partie
     */
    private void initJoin(boolean error) {
        clearWindow();

        JLabel addressLabel, portLabel, errorLabel;

        setJTextField(addressTextField, 200, 30, fontText);

        addressLabel = new JLabel("Saisir l'addresse de connection:");
        setJLabel(addressLabel, fontText, Color.WHITE);

        setJTextField(portTextField, 80, 30, fontText);

        portLabel = new JLabel("Saisir le port de connection:");
        setJLabel(portLabel, fontText, Color.WHITE);
        errorLabel = new JLabel("");
        setJLabel(errorLabel, fontText, Color.WHITE);
        if (error)
            errorLabel.setText("Erreur adresse/port, Réessayer!");


        GroupLayout groupLayout = new GroupLayout(contener);
        contener.setLayout(groupLayout);
        groupLayout.setLayoutStyle(new LayoutStyle() {
            @Override
            public int getPreferredGap(JComponent component1, JComponent component2, ComponentPlacement type, int position, Container parent) {
                return 20;
            }

            @Override
            public int getContainerGap(JComponent component, int position, Container parent) {
                return 60;
            }
        });
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(addressLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(addressTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(portLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(portTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(joinGame)
                                .addComponent(back))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(errorLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(addressLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(addressTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(PREFERED_GAP)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(portLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(portTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(PREFERED_GAP)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(joinGame)
                                .addComponent(back))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(errorLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

    }

    /**
     * Menu avec les informations utiles, en attendant que tous les joueur soient connectes.
     */
    private void displayInformationConnection() {
        clearWindow();

        JLabel jl = null;
        try {
            jl = new JLabel("<html><div style=\"text-align:center;\">" +
                    "En attente de joueurs<br><br> "+
                    "addresse Connection: " +  getClient().getAddressServeur() + "<br> Port: "+ getClient().getPort() + "</div></html>",JLabel.CENTER);
            setJLabel(jl, fontConnection, Color.WHITE);
            jl.setSize(new Dimension(300,150));
        } catch (IOException ignored) {}
        contener.setLayout(new BorderLayout());
        if (jl != null) {
            contener.add(jl, BorderLayout.CENTER);
        }
    }

    /**
     * Permet d'instantier l'hebergeur, creer tous les elements du pong a envoyer au autres joueurs
     */
    private void displayWaitPlayerHost() {
        int numberPlayer = Integer.parseInt(numberPlayerCombo.getSelectedItem().toString());
            if (1 < numberPlayer && numberPlayer < 5) {
                getClient().setMaxPlayer(numberPlayer);
                getClient().getPong().add(new Racket(1));
                getClient().getPong().add(new Ball(1));
                getClient().getPong().add(new Bonus());
                if(limitedScore.isSelected()) {
                    // si ce n'est pas un nombre alors la partie sera infini
                    try {
                        getClient().setMaxScore(Integer.parseInt(scoreMaxTextField.getText()));
                    } catch (Exception e) {
                        getClient().setMaxScore(0);
                    }
                }
                else {
                    getClient().setMaxScore(0);
                }
                displayInformationConnection();
            }
    }

    /**
     * Connecte un joueur a une partie.
     */
    private void displayWaitPlayerJoin() {
        try{
            String address = addressTextField.getText();
            int portConnection = Integer.parseInt(portTextField.getText());
            getClient().setNumberPlayer(1);
            getClient().connectionServerInit(address, portConnection, true);
        } catch (IOException | InterruptedException | UnresolvedAddressException | NumberFormatException e) {
            initJoin(true);
            return;
        }
        getClient().getPong().addKeyListener((Racket)getClient().getMyRacket());
        displayInformationConnection();
    }

    /**
     * Attend que tous les joueurs soient presents
     * @throws IOException
     * @throws InterruptedException
     */
    private void waitPlayer() throws IOException, InterruptedException {
        while (client.getNumberPlayer() != client.getMaxPlayer()) {
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
        if (e.getSource() == hosting) {
            initHosting();
        }
        if (e.getSource() == join) {
            initJoin(false);
        }
        if (e.getSource() == createGame) {
            displayWaitPlayerHost();
        }
        if (e.getSource() == joinGame) {
            displayWaitPlayerJoin();
        }
        if (e.getSource() == back) {
            clearTextField();
            menuMain();
        }
        if (e.getSource() == limitedScore) {
            scoreMaxTextField.setEnabled(true);
            scoreMaxTextField.setVisible(true);
        }
        if (e.getSource() == unlimitedScore) {
            scoreMaxTextField.setEnabled(false);
            scoreMaxTextField.setVisible(false);
        }
        this.repaint();
        this.validate();
    }

    /**
     * Enleve tous les composants de la fenetre (contener) et du panel text.
     */
    public void clearWindow() {
        contener.removeAll();
        text.removeAll();
    }

    /**
     * Nettoie la fenetre et vide tous les textfields. On l'utilise pour le bouton retour.
     */
    public void clearTextField() {
        clearWindow();
        addressTextField.setText("");
        portTextField.setText("");
        scoreMaxTextField.setText("");
    }

    /**
     * Initialise un JTextField.
     * @param jTextField le JTextField a modifier.
     * @param width La largeur du JTextField.
     * @param height La hauteur du JTextField.
     * @param font La police du JTextField.
     */
    public void setJTextField(JTextField jTextField, int width, int height, Font font) {
        jTextField.setPreferredSize(new Dimension(width, height));
        jTextField.setHorizontalAlignment(JTextField.CENTER);
        jTextField.setFont(font);
    }

    /**
     * Initialise un JLabel.
     * @param jLabel Le JLabel a modifier.
     * @param font La police du JLabel.
     * @param color La couleur du JLabel.
     */
    public void setJLabel(JLabel jLabel, Font font, Color color){
        jLabel.setFont(font);
        jLabel.setForeground(color);
    }

    /**
     * Initiale un JRadioButton.
     * @param jRadioButton Le JRadioButton a modifier..
     * @param opaque L'opacite du JRadioButton.
     * @param color La couleur du JRadioButton.
     * @param font La police du JRadioButton.
     */
    public void setRadioButton(JRadioButton jRadioButton, boolean opaque, Color color, Font font){
        jRadioButton.setPreferredSize(new Dimension(170,30));
        jRadioButton.setFont(font);
        jRadioButton.setForeground(color);
        jRadioButton.setOpaque(opaque);
    }


    /**
     * Change la taille par defaut du bouton et sa police.
     * @param button Le bouton que l'on modifie.
     * @param textSize La taille du texte sur le bouton.
     */
    private void setJButton(JButton button, int textSize, Dimension dim) {
        button.setPreferredSize(dim);
        button.setFont(new Font("Arial", Font.BOLD, textSize));
    }

    /**
     * Affiche le menu
     * @throws IOException
     * @throws InterruptedException
     */
    public void displayMenu() throws IOException, InterruptedException {
        menuMain();
        waitPlayer();
    }
}

