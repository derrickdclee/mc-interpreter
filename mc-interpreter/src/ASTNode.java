

abstract public class ASTNode {
	final String myNodeType;
	
	ASTNode(String nodeType) {
		myNodeType = nodeType;
	}
	
	public String toString() {
		return myNodeType;
	}
}

class RootNode extends ASTNode {
	IntNode i; // height - corresponds to y position
	IntNode j; // width - corresponds to x position
	ASTNode list;
	
	RootNode(IntNode i, IntNode j, ASTNode list) {
		super("root_node");
		this.i = i;
		this.j = j;
		this.list = list;
	}
}

class CatNode extends ASTNode {
	VarNode v;
	IntNode x;
	IntNode y;
	DirNode d;
	
	CatNode(VarNode v, IntNode x, IntNode y, DirNode d) {
		super("cat_node");
		this.v = v;
		this.x = x;
		this.y = y;
		this.d = d;
	}
}

class MouseNode extends ASTNode {
	VarNode v;
	IntNode i;
	IntNode j;
	DirNode d;
	
	MouseNode(VarNode v, IntNode i, IntNode j, DirNode d) {
		super("mouse_node");
		this.v = v;
		this.i = i;
		this.j = j;
		this.d = d;
	}
}

class HoleNode extends ASTNode {
	IntNode i;
	IntNode j;
	
	HoleNode(IntNode i, IntNode j) {
		super("hole_node");
		this.i = i;
		this.j = j;
	}
}

class SequenceNode extends ASTNode {
	ASTNode left;
	ASTNode right;
	
	SequenceNode(ASTNode left, ASTNode right) {
		super("seq_node");
		this.left = left;
		this.right = right;
	}
}

class MoveNode extends ASTNode {
	VarNode v;
	IntNode i;
	
	MoveNode(VarNode v, IntNode i) {
		super("move_node");
		this.v = v;
		this.i = i;
	}
}

class ClockwiseNode extends ASTNode {
	VarNode v;
	
	ClockwiseNode(VarNode v) {
		super("clockwise_node");
		this.v = v;
	}
}

class RepeatNode extends ASTNode {
	IntNode i;
	ASTNode list;
	
	RepeatNode(IntNode i, ASTNode list) {
		super("repeat_node");
		this.i = i;
		this.list = list;
	}
}

class IntNode extends ASTNode {
	Token intToken;
	
	IntNode(Token intToken) {
		super("int_leaf");
		this.intToken = intToken;
	}
	
	public String toString() {
		return intToken.getCharVal();
	}
}

class VarNode extends ASTNode {
	Token varToken;
	
	VarNode(Token varToken) {
		super("var_leaf");
		this.varToken = varToken;
	}
	
	public String toString() {
		return varToken.getCharVal();
	}
}

class DirNode extends ASTNode {
	Direction dir;
	
	DirNode(Token dirToken) {
		super("dir_leaf");
		switch (dirToken.getTokenType()) {
			case NORTH:
				this.dir = Direction.NORTH;
				break;
			case SOUTH:
				this.dir = Direction.SOUTH;
				break;
			case EAST:
				this.dir = Direction.EAST;
				break;
			case WEST:
				this.dir = Direction.WEST;
				break;
			default:
				break;
		}
	}
	
	public String toString() {
		return dir.name();
	}
}