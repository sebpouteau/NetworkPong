package src.Game;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * An Pong is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Pong extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int SIZE_PONG_X = 700;
    private static final int SIZE_PONG_Y = 700;
    private static final int SIZE_WINDOW_X = 700;
    private static final int SIZE_WINDOW_Y = 730;

    private Color colorJoueur1 = new Color(255, 229, 65);
    private Color colorJoueur2 = new Color(47, 37, 255);
    private Color colorJoueur3 = new Color(214, 30, 30);
    private Color colorJoueur4 = new Color(31, 197, 64);

    private int[] score;
    private int maxScore ;
    private ArrayList<PongItem> pongList;

    /**
     *  Est ce qu'il est arrete?
     */
    private static boolean startGame;

    /**
     * Est-ce que l'on peut le relancer?
     */
    private static boolean go;

    /**
     * Qui relance la balle?
     */
    private static int waitPlayer;

    /**
     * Constant (c.f. final) common to all Pong instances (c.f. static)
     * defining the background color of the Pong.
     */
    private static final Color backgroundColor = new Color(0, 0, 0);

    /**
     * Time step of the simulation (in ms).
     */
    public static final int timestep = 10;

    /**
     * Pixel data buffer for the Pong rendering.
     */
    private Image buffer = null;

    /**
     * Graphic component context derived from buffer Image.
     */
    private Graphics graphicContext = null;

    public Pong() {
        pongList = new ArrayList<>();
        this.setPreferredSize(new Dimension(SIZE_WINDOW_X, SIZE_WINDOW_Y));
        startGame = false;
        go = false;
        waitPlayer = 0;
    }

	/* =================================================
                      Getter and Setter
       ================================================= */

    public static int getSizePongX() {
        return SIZE_PONG_X;
    }

    public static int getSizePongY() {
        return SIZE_PONG_Y;
    }

    public int getScore(int i) {
        return score[i-1];
    }

    public void setScore(int i, int score) {
        this.score[i-1] = score;
    }

    public void setTabScore(int i) {
        score = new int[i];
    }

    public static boolean getIfGo() {
        return go;
    }

    public static void setIfGo(boolean bool) {
        go = bool;
    }

    public static boolean getIfStart() {
        return startGame;
    }

    public static void setIfStart(boolean bool){
        startGame = bool;
    }

    public static int getWaitPlayer() {
        return waitPlayer;
    }

    public static void  setWaitPlayer(int idPlayer) {
        waitPlayer = idPlayer;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

	 /* =================================================
                       Functions
        ================================================= */

    /**
     * Ajoute un pong item a la liste.
     * @param item Item a ajouter.
     */
    public void add(PongItem item) {
        if (item instanceof Ball) {
            int compteur=0;
            for (int i = 0; i < listItemSize(); i++) {
                if (getItem(i) instanceof Ball) {
                    compteur = Math.max(compteur, getItem(i).getNumber()) + 1;
                }
            }
            item.setNumber(compteur+1);
        }
        this.pongList.add(item);
    }

    /**
     * Lance la manche.
     */
    public void startGame() {
        if (getIfStart() && getIfGo()) {
            setIfStart(false);
            for (int i = 0; i < listItemSize(); i++) {
                if (getItem(i) instanceof Ball) {
                    Ball b = (Ball) getItem(i);
                    b.launch(getWaitPlayer());
                    setIfGo(false);
                }
            }
        }
    }

    /**
     * Supprime le pong item.
     * @param id Position de l'item a supprimer.
     */
    public void removeItem(int id) {
        pongList.remove(id);
    }

    /**
     * Retourne un pong Item present dans la liste.
     * @param number Numero de l'item a recuperer.
     * @return Un pongItem.
     */
    public PongItem getItem(int number) {
        return this.pongList.get(number);
    }

    /**
     * Retourne le nombre d'items de la liste.
     * @return Nombre d'items.
     */
    public int listItemSize() {
        return pongList.size();
    }

    /**
     * Anime les items du jeu.
     */
    public void animateItems() {
        startGame();
        for (int i = 0; i < listItemSize(); i++) {
            getItem(i).collision(pongList);
            getItem(i).animate(SIZE_PONG_X, SIZE_PONG_Y);
        }
        this.updateScreen();
    }

    /**
     * Verifie si un joueur a gagne.
     * @return Le numero du joueur qui a atteint le maximum de point, 0 sinon. Et 0 si il n'y a pas de maximum.
     */
    public boolean endGame() {
        if (maxScore == 0) {
            return false;
        }
        for (int i = 0; i < score.length; i++ ) {
            if(score[i] >= maxScore) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWin(int id) {
        if(getScore(id) == maxScore){
            return true;
        }
        return false;
    }
    /**
     * Dessine le contenu du buffer.
     * @param g Graphics.
     */
    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, this);
    }

    /**
     * Dessine les items.
     * @param item Un pongItem a dessiner.
     */
    public void draw(PongItem item) {
        graphicContext.drawImage(item.getImageItem(),
                item.getPositionX(), item.getPositionY(),
                item.getWidth(), item.getHeight(),
                null);
    }

    /**
     * Selectionne la couleur d'un joueur.
     * @param numero La raquette du joueur.
     * @return La couleur associe au joueur.
     */
    private Color selectColor(int numero) {
        switch (numero) {
            case 1:
                return colorJoueur1;
            case 2:
                return colorJoueur2;
            case 3:
                return colorJoueur3;
            case 4:
                return colorJoueur4;
            default:
                return Color.WHITE;
        }
    }

    /**
     * Met a jour l'ecran.
     */
    public void updateScreen() {
        if (buffer == null) {
			/* First time we get called with all windows initialized */
            buffer = createImage(SIZE_WINDOW_X,SIZE_WINDOW_Y);
            if (buffer == null) {
                throw new RuntimeException("Could not instanciate graphics");
            }
            else {
                graphicContext = buffer.getGraphics();
            }
        }

        graphicContext.setColor(backgroundColor);
        graphicContext.fillRect(0,0, SIZE_PONG_X, SIZE_PONG_Y);

        int sumNumberPlayer = 0;

		/* Draw items */
        for (int i = 0; i < listItemSize(); i++) {
            if (getItem(i) instanceof Racket){
                sumNumberPlayer += getItem(i).getNumber();
                graphicContext.setColor(selectColor(getItem(i).getNumber()));
                graphicContext.fillRect(getItem(i).getPositionX(), getItem(i).getPositionY(), getItem(i).getWidth(), getItem(i).getHeight());

            }
            else if (getItem(i) instanceof Bonus) {
                Bonus b = (Bonus) getItem(i);
                if(b.isVisible()){
                    draw(getItem(i));
                }
            }
            else {
                draw(getItem(i));
            }
        }

        graphicContext.setColor(Color.WHITE);
        if (sumNumberPlayer <= 3) {
            graphicContext.drawLine(SIZE_PONG_X / 2, 0, SIZE_PONG_X / 2, SIZE_PONG_Y);
        }
        else {
            graphicContext.drawLine(0, 0, SIZE_PONG_X, SIZE_PONG_Y);
            graphicContext.drawLine(SIZE_PONG_X , 0, 0, SIZE_PONG_Y);
        }

        graphicContext.setColor(Color.GRAY);
        graphicContext.fillRect(0, SIZE_PONG_Y,SIZE_WINDOW_X,SIZE_WINDOW_Y);
        for (int j = 0; j < listItemSize() ; j++) {
            if (getItem(j) instanceof Racket) {
                int idPlayer = getItem(j).getNumber();
                graphicContext.setFont(new Font("impact", Font.PLAIN, 20));
                graphicContext.setColor(selectColor(getItem(j).getNumber()));
                graphicContext.drawString("Joueur" + idPlayer + " : " + score[idPlayer - 1]+"   ", 80 + (idPlayer - 1)*150 , SIZE_PONG_Y + 20);
            }
        }
        this.repaint();
        this.validate();
    }

    /**
     * Affiche l'ecran de fin quan il n'y a plus de joueurs.
     */
    public void updateScreenEnd() {
        updateEND("Tous les joueurs sont déconnectés.");
    }

    /**
     * Affiche l'ecran de fin du gagnant.
     */
    public void updateScreenWin() {
        updateEND("Vous avez gagné!");
    }

    /**
     * Affiche l'ecran de fin du perdant.
     */
    public void updateScreenLose() {
        updateEND("Vous avez perdu! ");
    }

    /**
     * Affiche un ecran de fin avec un message.
     * @param string Le message a afficher.
     */
    private void updateEND(String string) {
        graphicContext.setColor(Color.BLACK);
        graphicContext.fillRect(0, SIZE_PONG_Y,SIZE_WINDOW_X,SIZE_WINDOW_Y);
        graphicContext.drawLine(SIZE_PONG_X/2,0,SIZE_PONG_Y/2,SIZE_PONG_Y);
        graphicContext.setColor(Color.WHITE);

        graphicContext.setFont(new Font("Arial", Font.PLAIN, 30));
        graphicContext.drawString(string, SIZE_PONG_X/2 - string.length() * 7, 250);
        graphicContext.setFont(new Font("Serif", Font.PLAIN, 80));
        graphicContext.drawString("FIN",SIZE_PONG_X/2 -70, 350);

        for (int j = 0; j < listItemSize() ; j++) {
            if (getItem(j) instanceof Racket) {
                int idPlayer = getItem(j).getNumber();
                graphicContext.setFont(new Font("impact", Font.PLAIN, 20));
                graphicContext.setColor(selectColor(getItem(j).getNumber()));
                graphicContext.drawString("Joueur" + idPlayer + " : " + score[idPlayer - 1]+"   ", 80 + (idPlayer - 1)*150 ,450);
            }
        }

        graphicContext.setColor(Color.WHITE);
        graphicContext.setFont(new Font("Arial", Font.PLAIN, 35));
        graphicContext.drawString("Veuillez fermer la fenêtre!", SIZE_PONG_X/2 - 24*7, 550);

        this.repaint();
        this.validate();
    }
}

