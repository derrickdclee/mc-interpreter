/*
 * Rules:
 * 1. Any reference to a dead critter is invalid.
 * 2. Any instantiation of a critter with the same variable name as another
 * critter (dead or alive) is invalid.
 * 3. Multiple mice cannot be on the same non-hole Spot.
 * -> kill the last to enter
 * 4. Multiple cats cannot be on the same Spot.
 * -> kill the last to enter.
 * 5. Mouse and Cat together
 * -> kill the mouse
 * 6. Holes must be instantiated on an empty Spot.
 */
public class ASTProcessor {
	private Grid myGrid;
	
	public void process(RootNode root) throws ASTTraversalException {
		myGrid = new Grid(root.i.intToken.getIntVal(), root.j.intToken.getIntVal());
		processNode(root.list);
	}
	
	private void processNode(ASTNode list) throws ASTTraversalException {
		switch (list.myNodeType) {
			case "cat_node":
				processCatNode((CatNode) list);
				break;
			case "mouse_node":
				processMouseNode((MouseNode) list);
				break;
			case "hole_node":
				processHoleNode((HoleNode) list);
				break;
			case "seq_node":
				processSequenceNode((SequenceNode) list);
				break;
			case "move_node":
				processMoveNode((MoveNode) list);
				break;
			case "clockwise_node":
				processClockwiseNode((ClockwiseNode) list);
				break;
			case "repeat_node":
				processRepeatNode((RepeatNode) list);
				break;
		}
	}
	
	private void processCatNode (CatNode node) throws ASTTraversalException {
		String name = node.v.varToken.getCharVal();
		
		// throw exception if the variable name has been used
		if (myGrid.myVarMap.containsKey(name)) {
			throw new ASTTraversalException("You cannot reuse a variable name.");
		}
		
		int x = node.x.intToken.getIntVal();
		int y = node.y.intToken.getIntVal();
		// throw exception if out of bounds
		if (!isValidLocation(x, y)) {
			throw new ASTTraversalException("You've gone off the grid.");
		}
		
		Cat c = new Cat(name, x, y, node.d);
		myGrid.myVarMap.put(name, c);
		
		Spot spot = myGrid.myGrid[x][y];
		if (!spot.isOccupied()) {
			spot.put(c);
		} else {
			Critter occupant = spot.getOccupant();
			if (occupant instanceof Cat) {
				c.kill();
			} else {
				// kill the mouse
				occupant.kill();
				spot.remove();
				spot.put(c);
			}
		}
	}
	
	private void processMouseNode(MouseNode node) throws ASTTraversalException {
		String name = node.v.varToken.getCharVal();
		
		// throw exception if the variable name has been used
		if (myGrid.myVarMap.containsKey(name)) {
			throw new ASTTraversalException("You cannot reuse a variable name.");
		}
		
		int x = node.x.intToken.getIntVal();
		int y = node.y.intToken.getIntVal();
		// throw exception if out of bounds
		if (!isValidLocation(x, y)) {
			throw new ASTTraversalException("You've gone off the grid.");
		}
		
		Mouse m = new Mouse(name, x, y, node.d);
		myGrid.myVarMap.put(name, m);
		
		Spot spot = myGrid.myGrid[x][y];
		if (spot.hasHole()) {
			spot.putInHole(m);
		} else {
			if (!spot.isOccupied()) {
				spot.put(m);
			} else {
				// regardless of whether the occupant is a cat or a mouse
				// this mouse dies
				m.kill();
			}
		}
	}
	
	private void processHoleNode(HoleNode node) throws ASTTraversalException {
		int x = node.x.intToken.getIntVal();
		int y = node.y.intToken.getIntVal();
		Spot spot = myGrid.myGrid[x][y];
		
		// throw exception if already has a hole or is occupied
		if (spot.hasHole() || spot.isOccupied()) {
			throw new ASTTraversalException("You cannot put a hole here.");
		}
		spot.makeHole();
	}
	
	private void processSequenceNode(SequenceNode node) throws ASTTraversalException {
		processNode(node.left);
		processNode(node.right);
	}
	
	private void processMoveNode(MoveNode node) throws ASTTraversalException {
		// throw exception if try to move an uninstantiated critter
		String name = node.v.varToken.getCharVal();
		if (!myGrid.myVarMap.containsKey(name)) {
			throw new ASTTraversalException("This variable has not been instantiated.");
		}
		
		// throw exception if try to move a dead critter
		Critter critter = myGrid.myVarMap.get(name);
		if (!critter.isAlive()) {
			throw new ASTTraversalException("This critter is dead.");
		}
		
		// get the current direction, current positions, and the distance it needs to travel
		Direction dir = critter.getDir();
		int oldX = critter.getX();
		int oldY = critter.getY();
		int x = oldX;
		int y = oldY;
		int distance = node.i.intToken.getIntVal();
		// compute the new location
		switch (dir) {
			case NORTH:
				x -= distance;
				break;
			case SOUTH:
				x += distance;
				break;
			case EAST:
				y += distance;
				break;
			case WEST:
				y -= distance;
				break;
		}
		// validate this location
		if (!isValidLocation(x, y)) {
			critter.kill();
			throw new ASTTraversalException("You've gone off the grid.");
		}
		
		// remove critter from old Spot
		Spot oldSpot = myGrid.myGrid[oldX][oldY];
		if (critter instanceof Cat || !oldSpot.hasHole()) {
			oldSpot.remove();
		} else {
			oldSpot.removeFromHole((Mouse) critter);
		}
		
		// try to put the critter in the new Spot
		Spot newSpot = myGrid.myGrid[x][y];
		if (critter instanceof Cat) {
			if (!newSpot.isOccupied()) {
				newSpot.put(critter);
			} else {
				Critter occupant = newSpot.getOccupant();
				if (occupant instanceof Cat) {
					critter.kill();
					return;
				} else {
					occupant.kill();
					newSpot.remove();
					newSpot.put(critter);
				}
			}
		} else {
			if (newSpot.hasHole()) {
				newSpot.putInHole((Mouse) critter); 
			} else {
				if (!newSpot.isOccupied()) {
					newSpot.put(critter);
				} else {
					critter.kill();
					return;
				}
			}
		}
		// update critter's location
		critter.updateX(x);
		critter.updateY(y);
	}
	
	private void processClockwiseNode(ClockwiseNode node) throws ASTTraversalException {
		// throw exception if try to move an uninstantiated critter
		String name = node.v.varToken.getCharVal();
		if (!myGrid.myVarMap.containsKey(name)) {
			throw new ASTTraversalException("This variable has not been instantiated.");
		}
		
		// throw exception if try to move a dead critter
		Critter critter = myGrid.myVarMap.get(name);
		if (!critter.isAlive()) {
			throw new ASTTraversalException("This critter is dead.");
		}
		
		Direction oldDir = critter.getDir();
		Direction newDir = null;
		switch(oldDir) {
			case NORTH:
				newDir = Direction.EAST;
				break;
			case EAST:
				newDir = Direction.SOUTH;
				break;
			case SOUTH:
				newDir = Direction.WEST;
				break;
			case WEST:
				newDir = Direction.NORTH;
				break;
		}
		critter.updateDir(newDir);
	}
	
	private void processRepeatNode(RepeatNode node) throws ASTTraversalException {
		int rep = node.i.intToken.getIntVal();
		ASTNode list = node.list;
		
		if (rep < 0) {
			throw new ASTTraversalException("The repetition number cannot be negative.");
		} else if (rep == 0) {
			return;
		}
		
		for (int i=0; i<rep; i++) {
			processNode(list);
		}
	}
	
	private boolean isValidLocation(int x, int y) {
		return x >= 0 && x < myGrid.myGrid.length &&
				y >= 0 && y < myGrid.myGrid[0].length;
	}
}

class ASTTraversalException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ASTTraversalException(String s) {
		super(s);
	}
}
