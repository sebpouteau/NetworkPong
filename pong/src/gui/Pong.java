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

	public static int getSizePongX() {
		return SIZE_PONG_X;
	}
	public static int getSizePongY(){
		return SIZE_PONG_Y;
	}

	/**
	/**
	 * Constant (c.f. final) common to all Pong instances (c.f. static)
	 * defining the background color of the Pong
	 */
	private static final Color backgroundColor = new Color(209, 209, 206);


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
			System.out.println(compteur);
		}
		this.pongList.add(item);
	}

	public PongItem getItem(int number){
		return this.pongList.get(number);
	}

	public int listItemSize(){
		return pongList.size();
	}

	public Pong() {
		pongList = new ArrayList<PongItem>();
		this.add(new Racket(3));
		this.add(new Ball(1,80, 80));
		this.add(new Ball(2,200, 200));
        this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(getItem(0));
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

	public void updateScreen() {
		if (buffer == null) {
			/* First time we get called with all windows initialized */
			buffer = createImage(SIZE_PONG_X,SIZE_PONG_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}

		/* Fill the area with blue */
		graphicContext.setColor(backgroundColor);
		graphicContext.fillRect(0, 0, SIZE_PONG_X, SIZE_PONG_Y);

		/* Draw items */
		for (int i = 0; i < listItemSize(); i++) {
			graphicContext.drawImage(getItem(i).getImageItem(),
					getItem(i).getPositionX(), getItem(i).getPositionY(),
					getItem(i).getWidth(), getItem(i).getHeight(),
					null);
		}
		this.repaint();
	}
}
