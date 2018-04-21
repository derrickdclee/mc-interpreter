/**
 * 
 * @author Derrick Lee <derrickdclee@gmail.com>
 *
 */

abstract public class ASTNode {
	private final ASTNodeType myNodeType;
	
	public ASTNode(ASTNodeType nodeType) {
		myNodeType = nodeType;
	}
	
	public ASTNodeType getNodeType() {
		return myNodeType;
	}
	
	public String toString() {
		return myNodeType.name();
	}
}

class RootNode extends ASTNode {
	IntLeaf height; // height - corresponds to y position
	IntLeaf width; // width - corresponds to x position
	ASTNode list;
	
	public RootNode(IntLeaf height, IntLeaf width, ASTNode list) {
		super(ASTNodeType.ROOT_NODE);
		this.height = height;
		this.width = width;
		this.list = list;
	}
}

class CatNode extends ASTNode {
	VarLeaf v;
	IntLeaf x;
	IntLeaf y;
	DirLeaf d;
	
	public CatNode(VarLeaf v, IntLeaf x, IntLeaf y, DirLeaf d) {
		super(ASTNodeType.CAT_NODE);
		this.v = v;
		this.x = x;
		this.y = y;
		this.d = d;
	}
}

class MouseNode extends ASTNode {
	VarLeaf v;
	IntLeaf x;
	IntLeaf y;
	DirLeaf d;
	
	public MouseNode(VarLeaf v, IntLeaf x, IntLeaf y, DirLeaf d) {
		super(ASTNodeType.MOUSE_NODE);
		this.v = v;
		this.x = x;
		this.y = y;
		this.d = d;
	}
}

class HoleNode extends ASTNode {
	IntLeaf x;
	IntLeaf y;
	
	public HoleNode(IntLeaf x, IntLeaf y) {
		super(ASTNodeType.HOLE_NODE);
		this.x = x;
		this.y = y;
	}
}

class SequenceNode extends ASTNode {
	ASTNode left;
	ASTNode right;
	
	public SequenceNode(ASTNode left, ASTNode right) {
		super(ASTNodeType.SEQUENCE_NODE);
		this.left = left;
		this.right = right;
	}
}

class MoveNode extends ASTNode {
	VarLeaf v;
	IntLeaf i;
	
	public MoveNode(VarLeaf v, IntLeaf i) {
		super(ASTNodeType.MOVE_NODE);
		this.v = v;
		this.i = i;
	}
}

class ClockwiseNode extends ASTNode {
	VarLeaf v;
	
	public ClockwiseNode(VarLeaf v) {
		super(ASTNodeType.CLOCKWISE_NODE);
		this.v = v;
	}
}

class RepeatNode extends ASTNode {
	IntLeaf i;
	ASTNode list;
	
	public RepeatNode(IntLeaf i, ASTNode list) {
		super(ASTNodeType.REPEAT_NODE);
		this.i = i;
		this.list = list;
	}
}

class IntLeaf extends ASTNode {
	Token intToken;
	
	public IntLeaf(Token intToken) {
		super(ASTNodeType.INT_LEAF);
		this.intToken = intToken;
	}
	
	public String toString() {
		return intToken.getCharVal();
	}
}

class VarLeaf extends ASTNode {
	Token varToken;
	
	public VarLeaf(Token varToken) {
		super(ASTNodeType.VAR_LEAF);
		this.varToken = varToken;
	}
	
	public String toString() {
		return varToken.getCharVal();
	}
}

class DirLeaf extends ASTNode {
	Direction dir;
	
	public DirLeaf(Token dirToken) {
		super(ASTNodeType.DIR_LEAF);
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