package src.gui;

        import src.Network.Player;

        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.io.IOException;
        import java.nio.channels.SocketChannel;

public class Menu extends JFrame implements ActionListener {

    private JPanel contener = new JPanel(){
        /* Creer un Jpanel d"grader de noir */
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

    private JTextField numberPlayerTextField = new JTextField();
    private JTextField adressTextField = new JTextField();
    private JTextField portTextField = new JTextField();

    private Player client;
    private int buttonTextSize = 25;
    private boolean allPlayerConnect = true;

    private final Dimension MENU_SIZE = new Dimension(500,400);

    private final Font fontTitle = new Font("Serif",Font.BOLD|Font.ITALIC,50);
    private final Font fontText= new Font("Arial",Font.PLAIN,25);
    private final Font fontConnection= new Font("Arial",Font.BOLD,25);

    private final Dimension dimensionButtonMenu = new Dimension(400,60);
    private final Dimension dimensionButton = new Dimension(300,50);


    public Menu(Player client) {
        this.client = client;
        this.setTitle("Pong");
        this.setSize(MENU_SIZE);
        this.getContentPane().add(contener);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        modificationButton(hosting,buttonTextSize,dimensionButtonMenu);
        modificationButton(join,buttonTextSize,dimensionButtonMenu);
        modificationButton(createGame,buttonTextSize, dimensionButton);
        modificationButton(joinGame,buttonTextSize, dimensionButton);
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
     * Change la taille par defaut du bouton et sa police
     * @param button le bouton que l'on modifie
     * @param textSize la taille du texte sur le bouton
     */
    private void modificationButton(JButton button, int textSize, Dimension dim){
        button.setPreferredSize(dim);
        button.setFont(new Font("Arial",Font.BOLD,textSize));
    }

    /**
     * Affiche le menu du jeu
     */
    public void menuMain(){
        JPanel space = createSpace(20);
        space.setOpaque(false);
        JPanel space1 = createSpace(20);
        space1.setOpaque(false);

        JLabel titre = new JLabel("PONG");
        titre.setForeground(Color.WHITE);
        text.setOpaque(false);
        text.setPreferredSize(new Dimension((int)MENU_SIZE.getWidth(),100));
        titre.setFont(fontTitle);
        text.add(titre,BorderLayout.CENTER);

        contener.add(space);
        contener.add(text) ;
        contener.add(hosting);
        contener.add(space1);
        contener.add(join);

        hosting.addActionListener(this);
        join.addActionListener(this);

        this.setVisible(true);
    }

    /**
     * masquera la fenetre menu
     */
    public void endMenu() {
        this.setVisible(false);
    }

    /**
     * Creer une zone d'espace permettant de decaler l'affichage par exemple
     * @param dimY dimension en hauteur
     * @return retourne une zone vide JPanel (espace)
     */
    private JPanel createSpace(int dimY){
        JPanel tmp = new JPanel();
        tmp.setPreferredSize(new Dimension((int)MENU_SIZE.getWidth(), dimY));
        tmp.setOpaque(false);
        return tmp;
    }

    /**
     * Menu concernant la partie ce creation de la partie
     */
    private void initHosting() {
        contener.removeAll(); // =============================================
        text.removeAll(); // ===============================================

        JPanel space = createSpace(20);
        JPanel spaceAfter = createSpace(20);

        JLabel numberPlayerLabel = new JLabel("<html><div style=\"text-align:center;\"> Saisir le nombre de joueurs<br> (max 4)<br></div></html>");
        numberPlayerLabel.setForeground(Color.WHITE);
        numberPlayerLabel.setFont(fontText);

        text.add(numberPlayerLabel,BorderLayout.CENTER);

        numberPlayerTextField.setPreferredSize(new Dimension(200, 30));
        numberPlayerTextField.setFont(fontText);
        createGame.addActionListener(this);

        contener.add(space);
        contener.add(text);
        contener.add(numberPlayerTextField,BorderLayout.AFTER_LAST_LINE);
        contener.add(spaceAfter);
        contener.add(createGame);
    }

    /**
     * Menu concernant la partie permettant de rejoindre une partie
     */
    private void initJoin() {
        contener.removeAll(); // ===============================================
        text.removeAll(); // ===============================================

        JPanel space = createSpace(60);
        JPanel space1 = createSpace(20);
        JPanel space2 = createSpace(20);

        JLabel adressLabel, portLabel;

        JPanel panAdress = new JPanel();
        panAdress.setOpaque(false);
        adressTextField.setPreferredSize(new Dimension(100,30));
        adressTextField.setFont(fontText);

        adressLabel = new JLabel("Saisir l'adresse de connection:");
        adressLabel.setForeground(Color.WHITE);
        adressLabel.setFont(fontText);
        panAdress.setLayout(new BorderLayout());

        JPanel panPort = new JPanel();
        panPort.setOpaque(false);
        portTextField.setPreferredSize(new Dimension(70,30));
        portTextField.setFont(fontText);

        portLabel = new JLabel("Saisir le port de connection:");
        portLabel.setFont(fontText);
        portLabel.setForeground(Color.WHITE);

        panAdress.add(adressLabel,BorderLayout.LINE_START);
        panAdress.add(adressTextField,BorderLayout.AFTER_LAST_LINE);
        panPort.add(portLabel);
        panPort.add(portTextField);

        contener.add(space);
        contener.add(panAdress);
        contener.add(space1);
        contener.add(panPort);
        joinGame.addActionListener(this);
        contener.add(space2);
        contener.add(joinGame);
    }

    /**
     * Menu avec les informations utiles, en attendant que tout les joueur soit connecte
     */
    private void displayInformationConnection(){
        contener.removeAll(); // ===============================================
        text.removeAll(); // ===============================================

        JLabel jl = null;
        try {
            jl = new JLabel("<html><div style=\"text-align:center;\">" +
                    "En attente de joueurs<br><br> "+
                    "Adresse Connection: " +  getClient().getAdressServeur() + "<br> Port: "+ getClient().getPort() + "</div></html>",JLabel.CENTER);
            jl.setFont(fontConnection);
            jl.setSize(new Dimension(300,150));
        } catch (IOException ignored) {
        }
        contener.setLayout(new BorderLayout());
        if (jl != null) {
            jl.setForeground(Color.WHITE);
            contener.add(jl, BorderLayout.CENTER);
        }
    }

    /**
     * permet d'instantion l'hebergeur, creer tout les elements du pong a envoyer au autre joueur
     */
    private void displayWaitPlayerHost(){
        if (numberPlayerTextField.getText().length() != 0) {   // ===============================================
            int numberPlayer = Integer.parseInt(numberPlayerTextField.getText());
            if (1 < numberPlayer && numberPlayer < 5) {
                System.out.println("je passe " + numberPlayer);

                getClient().setMaxPlayer(numberPlayer);
                getClient().getPong().add(new Racket(1));
                getClient().getPong().add(new Ball(1));
                getClient().getPong().add(new Bonus());

                displayInformationConnection();
            }
        }
    }

    /**
     * Conecte un joueur a une partie
     */
    private void displayWaitPlayerJoin(){
        contener.removeAll();  // ===============================================
        text.removeAll(); // ===============================================

        String adress = adressTextField.getText();
        int portConnection = Integer.parseInt(portTextField.getText());
        getClient().setNumberPlayer(1);

        try {
            getClient().connectionServerInit(adress, portConnection, true);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        getClient().getPong().addKeyListener((Racket)getClient().getMyRacket());
        displayInformationConnection();
        this.repaint();
    }

    /**
     * Attend que tout les joueur soit present
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
        if (e.getSource() == hosting) {
            initHosting();
        }
        if (e.getSource() == join) {
            initJoin();
        }
        if (e.getSource() == createGame) {
            displayWaitPlayerHost();
        }
        if (e.getSource() == joinGame) {
            displayWaitPlayerJoin();
        }
        this.repaint();
       this.validate();
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
