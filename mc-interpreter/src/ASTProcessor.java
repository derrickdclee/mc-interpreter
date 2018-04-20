
public class ASTProcessor {
	private Grid myGrid;
	
	public void processRootNode(RootNode root) {
		myGrid = new Grid(root.i.intToken.getIntVal(), root.j.intToken.getIntVal());
		processBody(root.list);
	}
	
	private void processBody(ASTNode list) {
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
		}
	}
	
	private void processCatNode(CatNode node) {
		String name = node.v.varToken.getCharVal();
		int i = node.x.intToken.getIntVal();
		int j = node.y.intToken.getIntVal();
		Cat c = new Cat(name, i, j, node.d);
		myGrid.myGrid[i][j].myObjects.add(c);
		myGrid.myVarMap.put(name, c);
	}
	
	private void processMouseNode(MouseNode node) {
		String name = node.v.varToken.getCharVal();
		int i = node.i.intToken.getIntVal();
		int j = node.j.intToken.getIntVal();
		Mouse m = new Mouse(name, i, j, node.d);
		myGrid.myGrid[i][j].myObjects.add(m);
		myGrid.myVarMap.put(name, m);
	}
	
	private void processHoleNode(HoleNode node) {
		// TODO: what to do if the Spot had critters on it?
		int i = node.i.intToken.getIntVal();
		int j = node.j.intToken.getIntVal();
		myGrid.myGrid[i][j].isHole = true;
	}
	
	private void processSequenceNode(SequenceNode node) {
		processBody(node.left);
		processBody(node.right);
	}
	
	private void processMoveNode(MoveNode node) {
		// first get the Critter object associated with the variable name
		Critter critter = myGrid.myVarMap.get(node.v.varToken.getCharVal());
		// get the current direction, current positions, and the distance it needs to travel
		Direction dir = critter.getDir();
		int x = critter.getX();
		int y = critter.getY();
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
			// throw an error
		}
		// update critter's location
		// remove critter from old set
		// put the critter in the new set
	}
	
	private boolean isValidLocation(int x, int y) {
		// TODO: implement
		return true;
	}
}
