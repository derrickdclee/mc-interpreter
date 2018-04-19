
abstract public class ASTNode {
	final int myNodeType;
	
	ASTNode(int nodeType) {
		myNodeType = nodeType;
	}
}

class RootNode extends ASTNode {
	IntNode i;
	IntNode j;
	ASTNode list;

	RootNode(IntNode i, IntNode j, ASTNode list) {
		super(0);
		this.i = i;
		this.j = j;
		this.list = list;
	}
}

class CatNode extends ASTNode {
	int myNodeType = 1;
	VarNode v;
	IntNode i;
	IntNode j;
	DirNode d;
	
	
	CatNode(VarNode v, IntNode i, IntNode j, DirNode d) {
		super(1);
		this.v = v;
		this.i = i;
		this.j = j;
		this.d = d;
	}
}

class MouseNode extends ASTNode {
	int myNodeType = 2;
	VarNode v;
	IntNode i;
	IntNode j;
	DirNode d;
	
	MouseNode(VarNode v, IntNode i, IntNode j, DirNode d) {
		super(2);
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
		super(3);
		this.i = i;
		this.j = j;
	}
}

class SequenceNode extends ASTNode {
	ASTNode left;
	ASTNode right;
	
	SequenceNode(ASTNode left, ASTNode right) {
		super(4);
		this.left = left;
		this.right = right;
	}
}

class MoveNode extends ASTNode {
	VarNode v;
	IntNode i;
	
	MoveNode(VarNode v, IntNode i) {
		super(5);
		this.v = v;
		this.i = i;
	}
}

class ClockwiseNode extends ASTNode {
	VarNode v;
	
	ClockwiseNode(VarNode v) {
		super(6);
		this.v = v;
	}
}

class RepeatNode extends ASTNode {
	IntNode i;
	ASTNode list;
	
	RepeatNode(IntNode i, ASTNode list) {
		super(7);
		this.i = i;
		this.list = list;
	}
}

class IntNode extends ASTNode {
	Token i;
	
	IntNode(Token i) {
		super(8);
		this.i = i;
	}
}

class VarNode extends ASTNode {
	Token v;
	
	VarNode(Token v) {
		super(9);
		this.v = v;
	}
}

class DirNode extends ASTNode {
	Token d;
	
	DirNode(Token d) {
		super(10);
		this.d = d;
	}
}