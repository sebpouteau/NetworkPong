package src.gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * An Pong is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Pong extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int SIZE_PONG_X = 800;
	private static final int SIZE_PONG_Y = 600;
	private static final int SIZE_WINDOW_X = 800;
	private static final int SIZE_WINDOW_Y = 630;

	private int[] score;
	private ArrayList<PongItem> pongList;

    /**
     *  Est ce qu'il est arreter
     */
    private static boolean startGame;

    /**
     * Est-ce que l'on peut le relancer
     */
    private static boolean go;

    /**
     * qui relance la balle
     */
    private static int waitPlayer;

	 /**
	 * Constant (c.f. final) common to all Pong instances (c.f. static)
	 * defining the background color of the Pong
	 */
	private static final Color backgroundColor = new Color(0, 0, 0);


	/**
	 * Time step of the simulation (in ms)
	 */
	public static final int timestep = 10;

	/**
	 * Pixel data buffer for the Pong rendering
	 */
	private Image buffer = null;

	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;

	public Pong() {
		pongList = new ArrayList<PongItem>();
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

	public static int getSizePongY(){
		return SIZE_PONG_Y;
	}

	public int getScore(int i){return score[i-1];}

	public void setScore(int i, int score){this.score[i-1] = score;}

    public int[] getTabScore(){return score;}

    public void setTabScore(int i){score = new int[i];}

    public static boolean getIfGo(){return go;}

    public static void setIfGo(boolean bool){go = bool;}

    public static boolean getIfStart(){return startGame;}

    public static int getWaitPlayer(){return waitPlayer;}

    public static void  setWaitPlayer(int idPlayer){ waitPlayer = idPlayer;}

    public static void setIfStart(boolean bool){
        startGame = bool;
    }

	 /* =================================================
                       Functions
        ================================================= */

	/**
	 *
	 * @param item
	 */
	public void add(PongItem item){
		if (item instanceof Ball) {
			int compteur=0;
			for (int i = 0; i < listItemSize(); i++) {
				if (getItem(i) instanceof Ball)
					compteur = Math.max(compteur,getItem(i).getNumber())+1;
			}
			item.setNumber(compteur++);
		}
		this.pongList.add(item);
	}

	/**
	 *
	 * @param idRacket
	 */
    public void startGame(int idRacket) {
      if(getIfStart() && getIfGo()) {
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
	 *
	 * @param id
	 */
	public void removeItem(int id){
		pongList.remove(id);
	}

	/**
	 *
	 * @param number
	 * @return
	 */
	public PongItem getItem(int number){
		return this.pongList.get(number);
	}

	/**
	 *
	 * @return
	 */
	public int listItemSize(){
		return pongList.size();
	}

	/**
	 *
	 */
	public void animateItems() {
       startGame(getWaitPlayer());

        for(int i = 0; i < listItemSize(); i++) {
            getItem(i).collision(pongList);
            getItem(i).animate(SIZE_PONG_X, SIZE_PONG_Y);
        }
            this.updateScreen();
	}

	/**
	 *
	 * @param g
	 */
	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

	/**
	 *
	 * @param item
	 */
    public void draw(PongItem item){
        graphicContext.drawImage(item.getImageItem(),
                item.getPositionX(), item.getPositionY(),
                item.getWidth(), item.getHeight(),
                null);
    }

	/**
	 *
	 */
	public void updateScreen() {
		if (buffer == null) {
			/* First time we get called with all windows initialized */
			buffer = createImage(SIZE_WINDOW_X,SIZE_WINDOW_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}

		/* Fill the area with blue */
		graphicContext.setColor(Color.WHITE);
		graphicContext.fillRect(0, 0, SIZE_WINDOW_X, SIZE_WINDOW_Y);
        graphicContext.setColor(backgroundColor);
        graphicContext.fillRect(0,0, SIZE_PONG_X, SIZE_PONG_Y);

		/* Draw items */
		for (int i = 0; i < listItemSize(); i++) {
			if(getItem(i) instanceof Racket){
                graphicContext.setColor(new Color(197, 200, 60));
				graphicContext.fillRect(getItem(i).getPositionX(), getItem(i).getPositionY(), getItem(i).getWidth(), getItem(i).getHeight());
                graphicContext.setColor(backgroundColor);
			}
			else if (getItem(i) instanceof Bonus) {
                Bonus b =(Bonus) getItem(i);
				if(b.isVisible()){
					draw(getItem(i));
				}
            }
            else{
				draw(getItem(i));
			}
		}
        graphicContext.setColor(Color.BLACK);

        for (int j = 0; j < listItemSize() ; j++) {
            if(getItem(j) instanceof Racket){
                 int idPlayer = getItem(j).getNumber();
                graphicContext.drawString("Joueur " + (idPlayer) + " : " + score[idPlayer - 1]+"   ", 0 + (idPlayer - 1)*110 , SIZE_PONG_Y + 20);
				graphicContext.setFont(new Font("impact", Font.BOLD, 20));
			}
        }
        this.repaint();

	}
}

