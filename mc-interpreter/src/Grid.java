/**
 * @author Derrick Lee <derrickdclee@gmail.com>
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Grid {
	
	private Spot[][] myGrid;
	private Map<String, Critter> myVarMap;
	private int myHeight;
	private int myWidth;
	
	public Grid(int height, int width) {
		myHeight = height;
		myWidth = width;
		myGrid = new Spot[width][height]; // careful with the order
		myVarMap = new HashMap<>();
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				myGrid[x][y] = new Spot();
			}
		}
	}
	
	public Spot getSpot(int x, int y) {
		return myGrid[x][y];
	}
	
	public Map<String, Critter> getVarMap() {
		return myVarMap;
	}
	
	public int getHeight() {
		return myHeight;
	}
	
	public int getWidth() {
		return myWidth;
	}
}

class Spot {
	private Critter occupant;
	private Set<Critter> hole;
	private boolean hasHole;
	
	public Spot() {
		hasHole = false;
		hole = null;
		occupant = null;
	}
	
	public void put(Critter critter) {
		occupant = critter;
	}
	
	public void remove() {
		occupant = null;
	}
	
	public void putInHole(Mouse mouse) {
		hole.add(mouse);
	}
	
	public void removeFromHole(Mouse mouse) {
		hole.remove(mouse);
	}
	
	public void makeHole() {
		// assuming that the Spot is unoccupied
		hasHole = true;
		hole = new HashSet<>(); // instantiate the hole
	}
	
	public boolean hasHole() {
		return hasHole;
	}
	
	public boolean isOccupied() {
		return occupant != null;
	}

	public Critter getOccupant() {
		return occupant;
	}
}
