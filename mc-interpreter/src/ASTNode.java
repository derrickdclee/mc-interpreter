import java.util.ArrayList;
import java.util.List;

abstract public class ASTNode {
	final String myNodeType;
	List<ASTNode> myChildren;
	
	ASTNode(String nodeType) {
		myNodeType = nodeType;
		myChildren = new ArrayList<>();
	}
	
	public String toString() {
		return myNodeType;
	}
}

class RootNode extends ASTNode {
	RootNode(IntNode i, IntNode j, ASTNode list) {
		super("root_node");
		myChildren.add(i);
		myChildren.add(j);
		myChildren.add(list);
	}
}

class CatNode extends ASTNode {
	CatNode(VarNode v, IntNode i, IntNode j, DirNode d) {
		super("cat_node");
		myChildren.add(v);
		myChildren.add(i);
		myChildren.add(j);
		myChildren.add(d);
	}
}

class MouseNode extends ASTNode {
	MouseNode(VarNode v, IntNode i, IntNode j, DirNode d) {
		super("mouse_node");
		myChildren.add(v);
		myChildren.add(i);
		myChildren.add(j);
		myChildren.add(d);
	}
}

class HoleNode extends ASTNode {
	HoleNode(IntNode i, IntNode j) {
		super("hole_node");
		myChildren.add(i);
		myChildren.add(j);
	}
}

class SequenceNode extends ASTNode {
	SequenceNode(ASTNode left, ASTNode right) {
		super("seq_node");
		myChildren.add(left);
		myChildren.add(right);
	}
}

class MoveNode extends ASTNode {
	MoveNode(VarNode v, IntNode i) {
		super("move_node");
		myChildren.add(v);
		myChildren.add(i);
	}
}

class ClockwiseNode extends ASTNode {
	ClockwiseNode(VarNode v) {
		super("clockwise_node");
		myChildren.add(v);
	}
}

class RepeatNode extends ASTNode {
	RepeatNode(IntNode i, ASTNode list) {
		super("repeat_node");
		myChildren.add(i);
		myChildren.add(list);
	}
}

class IntNode extends ASTNode {
	Token i;
	
	IntNode(Token i) {
		super("int_leaf");
		this.i = i;
	}
	
	public String toString() {
		return i.getCharVal();
	}
}

class VarNode extends ASTNode {
	Token v;
	
	VarNode(Token v) {
		super("var_leaf");
		this.v = v;
	}
	
	public String toString() {
		return v.getCharVal();
	}
}

class DirNode extends ASTNode {
	Token d;
	
	DirNode(Token d) {
		super("dir_leaf");
		this.d = d;
	}
	
	public String toString() {
		return d.getTokenType().name();
	}
}