import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GridGraphic extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel [][] squares;
	private int myHeight;
	private int myWidth;
	private final String CAT_PATH = "./images/cat.jpg";
	private final String MOUSE_PATH = "./images/mouse.jpg";
	private final String HOLE_PATH = "./images/hole.jpg";
	
	public GridGraphic(int height, int width) {
		myHeight = height;
		myWidth = width;
		
		Container c = getContentPane();
        c.setLayout(new GridLayout(height, width, 1 , 1)); 
        squares = new JPanel[height][width];
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                squares[i][j] = new JPanel();
                squares[i][j].setBackground(Color.white);
                c.add(squares[i][j]);
            }
        }
	}
	
	public void updateGraphic(Grid grid) {
		for(int y=0; y<myHeight; y++) {
            for(int x=0; x<myWidth; x++) {
            	
            		Spot spot = grid.myGrid[x][y];
            		squares[y][x].removeAll();
                if (spot.isOccupied()) {
                		Critter occupant = spot.getOccupant();
                		if (occupant instanceof Cat) {
                			addPiece(CAT_PATH, y, x);
                		} else {
                			addPiece(MOUSE_PATH, y, x);
                		}
                } else {
                		if (spot.hasHole()) {
                			addPiece(HOLE_PATH, y, x);
                		}
                }
                
            }
        }
		revalidate();
		repaint();
	}
	
	public void addPiece(String filepath, int i, int j) {
		File file = new File(filepath);
		ImageIcon img = new ImageIcon(new ImageIcon(
				file.getAbsolutePath()).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));      
		JLabel label = new JLabel("", img, SwingConstants.CENTER); 
        squares[i][j].add(label, BorderLayout.CENTER);
    }
	
}
