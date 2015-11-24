package src.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

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
     * 15 secondes en millisecondes
     */
	private int bonusDelay = 30 * 1000;
    private long time;
	/**
	 * Pixel data buffer for the Pong rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;
    private Bonus bonus;
	private ArrayList<PongItem> pongList;


	public void add(PongItem item){
		if (item instanceof Ball) {
			int cpt=0;
			for (int i = 0; i < listItemSize(); i++) {
				if (getItem(i) instanceof Ball)
					cpt = Math.max(cpt,getItem(i).getNumber())+1;
			}
			item.setNumber(cpt++);
			System.out.println(cpt);
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
        bonus = new Bonus();
        time = System.currentTimeMillis();
        this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
	}

	public void animateItem() {
		for (int i = 0; i < listItemSize(); i++) {
			getItem(i).collision(pongList);
			getItem(i).animate(SIZE_PONG_X,SIZE_PONG_Y);
		}
//        if(time + bonusDelay < System.currentTimeMillis() && !bonus.getIsVisible()){
//            System.out.println("coucou");
//            bonus.appearance(SIZE_PONG_X/2, SIZE_PONG_Y/2);
//            time = System.currentTimeMillis();
//        }
//        if(bonus.getIsVisible()) {
//            bonus.animate(getSizePongX(), getSizePongY());
//            bonus.collision(pongList);
//        }

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
		JLabel text = new JLabel("coucou");
//        if(bonus.getIsVisible()){
//            graphicContext.drawImage(bonus.getImageItem(),bonus.getPositionX(), bonus.getPositionY(),
//            bonus.getWidth(), bonus.getHeight(),null);
//        }
		this.repaint();
	}
}
