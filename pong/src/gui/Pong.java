package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * An Pong is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Pong extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Width of src area
	 */
	private static final int SIZE_PONG_X = 800;
	/**
	 * Height of src area
	 */
	private static final int SIZE_PONG_Y = 600;
	private static final int SIZE_WINDOW_X = 800;
	private static final int SIZE_WINDOW_Y = 630;

	private int[] score;

	public static int getSizePongX() {
		return SIZE_PONG_X;
	}
	public static int getSizePongY(){
		return SIZE_PONG_Y;
	}

	public int getScore(int i){return score[i];}
	public void setScore(int i, int score){this.score[i] = score;}
    public int[] getTabScore(){return score;}
    public void setTabScore(int i){score = new int[i];}

	/**
	/**
	 * Constant (c.f. final) common to all Pong instances (c.f. static)
	 * defining the background color of the Pong
	 */
	private static final Color backgroundColor = new Color(86, 208, 209);


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
	private ArrayList<PongItem> pongList;

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
	public void removeItem(int id){
		pongList.remove(id);
	}

	public PongItem getItem(int number){
		return this.pongList.get(number);
	}

	public int listItemSize(){
		return pongList.size();
	}

	public Pong() {
		pongList = new ArrayList<PongItem>();
        this.setPreferredSize(new Dimension(SIZE_WINDOW_X, SIZE_WINDOW_Y));
	}

	public void animateItem() {
		for (int i = 0; i < listItemSize(); i++) {
            getItem(i).collision(pongList);
			getItem(i).animate(SIZE_PONG_X,SIZE_PONG_Y);
		}
		this.updateScreen();
	}

	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

    public void draw(PongItem pi){
        graphicContext.drawImage(pi.getImageItem(),
                pi.getPositionX(), pi.getPositionY(),
                pi.getWidth(), pi.getHeight(),
                null);
    }

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
                graphicContext.setColor(new Color(163,73,162));
				graphicContext.fillRect(getItem(i).getPositionX(), getItem(i).getPositionY(), getItem(i).getWidth(), getItem(i).getHeight());
                graphicContext.setColor(backgroundColor);
			}
			else if (getItem(i) instanceof Bonus) {
                Bonus b =(Bonus) getItem(i);
				if(b.isVisible()){
					draw(getItem(i));
				}
                 else if(b.isActive() && b.getNumber() == 3){
					Rock r = (Rock)b.getBonus();
                    graphicContext.setColor(Color.DARK_GRAY);
                    graphicContext.fillOval((int) r.getRock().getX(),(int) r.getRock().getY(), (int) r.getRock().getWidth(),(int) r.getRock().getHeight());
                    graphicContext.setColor(backgroundColor);
                }
            }
            else{
				draw(getItem(i));
			}
		}
        graphicContext.setColor(Color.BLACK);
        for (int i = 0; i < score.length ; i++) {
            graphicContext.drawString("Joueur" + (i+1) + ": " + score[i], 0 + i*100, SIZE_PONG_Y + 10);
        }
        this.repaint();
	}
}
