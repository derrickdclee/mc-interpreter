import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Grid {
	
	class Spot {
		Set<Critter> myObjects;
		boolean isHole;
		
		public Spot() {
			myObjects = new HashSet<>();
			isHole = false;
		}
		
		public void addGridObject(Critter obj) {
			myObjects.add(obj);
		}
		
		public void removeGridObject(Critter obj) {
			myObjects.remove(obj);
		}
		
		public void makeHole() {
			this.isHole = true;
		}
	}
	
	Spot[][] myGrid;
	Map<String, Critter> myVarMap;
	
	public Grid(int i, int j) {
		myGrid = new Spot[i][j];
		myVarMap = new HashMap<>();
		
		for (int row=0; row<i; row++) {
			for (int col=0; col<j; col++) {
				myGrid[row][col] = new Spot();
			}
		}
	}
}
