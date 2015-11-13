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

	public ArrayList<PongItem> pongList;


	public void add(PongItem item){
		this.pongList.add(item);
	}

	public Pong() {
		pongList = new ArrayList<PongItem>();
		//pongList.add(new Racket());
		pongList.add(new Racket(1));
		pongList.add(new Ball());
		//	pongList.add(new Ball(80,80));
		//	pongList.add(new Ball(500,500));
		//	pongList.add(new Ball(689, 522));
		//	pongList.add(new Racket(760,560));

		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(pongList.get(0));
	}

	public void animateItem() {
		for (int i = 0; i < pongList.size(); i++) {
			pongList.get(i).collision(pongList);
			pongList.get(i).animate(SIZE_PONG_X,SIZE_PONG_Y);
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
		for (int i = 0; i < pongList.size(); i++) {
			graphicContext.drawImage(pongList.get(i).getImageItem(),
					pongList.get(i).getPositionX(), pongList.get(i).getPositionY(),
					pongList.get(i).getWidth(), pongList.get(i).getHeight(),
					null);
		}
		this.repaint();
	}
}
